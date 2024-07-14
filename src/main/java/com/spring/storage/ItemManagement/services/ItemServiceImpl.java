package com.spring.storage.ItemManagement.services;

import com.spring.storage.CategoryManagement.model.Category;
import com.spring.storage.CategoryManagement.repositories.CategoryRepository;
import com.spring.storage.Exceptions.ConflictException;
import com.spring.storage.Exceptions.NotFoundException;
import com.spring.storage.FileManagement.FileStorageService;
import com.spring.storage.ItemManagement.api.CreateItemRequest;
import com.spring.storage.ItemManagement.api.EditItemAmountRequest;
import com.spring.storage.ItemManagement.api.EditItemRequest;
import com.spring.storage.ItemManagement.model.Item;
import com.spring.storage.ItemManagement.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{


    private final ItemRepository itemRepository;

    private final CategoryRepository categoryRepository;

    private final EditItemMapper editItemMapper;

    private final FileStorageService fileService;
    public Page<Item> findAll(int page, String field, boolean ascending){
        Sort.Direction sortDirection = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;
        return itemRepository.findAll(PageRequest.of(page - 1, 10, sortDirection, field));
    }

    public Page<Item> queryByName(String query, int page,int size, String field, boolean ascending){
        Sort.Direction sortDirection = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;
        return itemRepository.findByNameContainingIgnoreCase(query, PageRequest.of(page - 1, size, sortDirection, field));
    }

    public Page<Item> queryShoppingList(String query,int page,int size, String field, boolean ascending){
        Sort.Direction sortDirection = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;
        return itemRepository.findByNameContainingIgnoreCaseAndNeedsShopping(query, PageRequest.of(page - 1, size, sortDirection, field));
    }

    public Page<Item> queryOutOfStock(String query,int page,int size, String field, boolean ascending){
        Sort.Direction sortDirection = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;
        return itemRepository.findByNameContainingIgnoreCaseAndOutOfStock(query, PageRequest.of(page - 1, size, sortDirection, field));
    }

    public Page<Item> queryNeedCheckup(String query,int page,int size, String field, boolean ascending){
        Sort.Direction sortDirection = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;
        return itemRepository.findByNameContainingIgnoreCaseAndNeedsCheckup(query, PageRequest.of(page - 1, size, sortDirection, field));
    }
    public Page<Item> queryByNameAndCategoryId(String query,Long id, int page,int size, String field, boolean ascending){
        Sort.Direction sortDirection = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;
        return itemRepository.findByNameContainingIgnoreCaseAndCategoryCatPk(query ,id ,PageRequest.of(page - 1, size, sortDirection, field));
    }

    public Page<Item> queryByNameAndFavoriteTrue(String query, int page,int size, String field, boolean ascending){
        Sort.Direction sortDirection = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;
        return itemRepository.findByNameContainingIgnoreCaseAndFavoriteTrue(query ,PageRequest.of(page - 1, size, sortDirection, field));
    }

    public Item findByName(String name){
        Optional<Item> item = itemRepository.findByName(name);
        if(item.isPresent()) {
            return item.get();
        }else {
            throw new NotFoundException("There are no Items with this name");
        }
    }

    public Item findById(Long id){
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("There is no Item with the specified Id"));

    }

    public Page<Item> findByCategory(Long catId, int page, String field, boolean ascending){
        Optional<Category> cat = categoryRepository.findById(catId);
        Sort.Direction sortDirection = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;
        if(cat.isPresent()) {
            return itemRepository.findByCategory(cat.get(), PageRequest.of(page - 1, 10, sortDirection, field));
        }else {
            throw new NotFoundException("This category doesn't exist");
        }
    }

    public Item create(CreateItemRequest createItemRequest){
        if(itemRepository.findByName(createItemRequest.getName()).isPresent()){
            throw new DataIntegrityViolationException("The Item with the name '" + createItemRequest.getName() + "' already exists");
        }else {
            final Item item = editItemMapper.create(createItemRequest);
            return itemRepository.save(item);
        }
    }

    public Item updateItem(long desiredVersion, Long id, EditItemRequest editItemRequest){
        Item item = findById(id);

        if(itemRepository.findByName(editItemRequest.getName()).isPresent()){
            throw new DataIntegrityViolationException("The Item with the name '" + editItemRequest.getName() + "' already exists");
        }else {
            final var cat = categoryRepository.findByName(editItemRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("There is no Category with the specified name"));
            item.updateItem(desiredVersion,editItemRequest.getName(), editItemRequest.getDescription(), cat, editItemRequest.getFavorite(), editItemRequest.getUpdateFrequency());
            return itemRepository.save(item);
        }
    }

    public Item updateAmount(long desiredVersion, EditItemAmountRequest editItemAmountRequest) {

        Item item = findById(editItemAmountRequest.getId());
        item.setAmount(desiredVersion,editItemAmountRequest.getAmount());
        return itemRepository.save(item);
    }

    public Item addAmount(long desiredVersion, EditItemAmountRequest editItemAmountRequest){
        Item item = findById(editItemAmountRequest.getId());
        item.addAmount(desiredVersion, editItemAmountRequest.getAmount());
        return itemRepository.save(item);
    }

    public Item favoriteItem(long desiredVersion, Long id){
        Item item = findById(id);
        item.toggleFavorite(desiredVersion);
        return itemRepository.save(item);
    }

    public Item toggleNeedsShopping(long desiredVersion, Long id){
        Item item = findById(id);
        item.toggleShopping(desiredVersion);
        return itemRepository.save(item);
    }

    public Item fileSave(Item itemIni, String imageURI, long desiredVersion){

        final var item = deleteAssociatedPhoto(itemIni);
        item.updateImage(desiredVersion,imageURI);
        return itemRepository.save(item);
    }

    public Item refreshLastModified(Long itemId, long desiredVersion){
        final var item = findById(itemId);
        item.refreshLastModified();
        return itemRepository.save(item);
    }

    public Item deleteAssociatedPhoto(Item item) {
        String image = item.getImage();
        if (image != null) {
            String fileName = image.substring(image.lastIndexOf("/") + 1);
            fileService.deleteFile(fileName);
            item.setImage(null);
            return item;
        }
        return item;
    }

    public Item checkVersion(Long version, long itemId){
        final var item = findById(itemId);
        if (item.getVersion() != version) {
            throw new ConflictException("Object was modified by another user");
        }
        return item;
    }
}
