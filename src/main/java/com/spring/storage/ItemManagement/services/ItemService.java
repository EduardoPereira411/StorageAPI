package com.spring.storage.ItemManagement.services;

import com.spring.storage.CategoryManagement.model.Category;
import com.spring.storage.ItemManagement.api.CreateItemRequest;
import com.spring.storage.ItemManagement.api.EditItemAmountRequest;
import com.spring.storage.ItemManagement.api.EditItemRequest;
import com.spring.storage.ItemManagement.model.Item;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ItemService {

    Page<Item> findAll(int page, String field, boolean ascending);

    Item findById(Long id);

    Item findByName(String name);

    Page<Item> findByCategory(Long catId, int page, String field, boolean ascending);

    Page<Item> queryByName(String query, int page, String field, boolean ascending);

    Page<Item> queryShoppingList(String query,int page, String field, boolean ascending);

    Page<Item> queryOutOfStock(String query,int page, String field, boolean ascending);

    Page<Item> queryNeedCheckup(String query,int page, String field, boolean ascending);

    Page<Item> queryByNameAndCategoryId(String query,Long id, int page, String field, boolean ascending);

    Page<Item> queryByNameAndFavoriteTrue(String query, int page, String field, boolean ascending);

    Item create(CreateItemRequest createItemRequest);

    Item updateItem(long desiredVersion, Long id,EditItemRequest editItemRequest);

    Item updateAmount(long desiredVersion, EditItemAmountRequest editItemRequest);

    Item addAmount(long desiredVersion, EditItemAmountRequest editItemAmountRequest);

    Item toggleNeedsShopping(long desiredVersion, Long id);

    Item favoriteItem(long desiredVersion, Long id);

    Item fileSave(Item itemIni, String imageURI, long desiredVersion);

    Item refreshLastModified(Long itemId, long desiredVersion);

    Item deleteAssociatedPhoto(Item item);

    Item checkVersion(Long version, long itemId);
}
