package com.spring.storage.CategoryManagement.api;

import com.spring.storage.CategoryManagement.model.Category;
import com.spring.storage.ItemManagement.api.ItemPartialView;
import com.spring.storage.ItemManagement.model.Item;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public abstract class CategoryViewMapper {

    public abstract CategoryView toCategoryView(Category category);

    public abstract Iterable<CategoryView> toCategoryView(Iterable<Category> categories);

    @AfterMapping //adiciona a quantidade de items diferentes numa categoria
    protected void calculateItemCount(Category category, @MappingTarget CategoryView categoryView) {
        categoryView.setItemCount(category.getItems() != null ? category.getItems().size() : 0);
    }

    public CategoryFullView toCategoryFullView(Category category, Page<Item> items){
        CategoryFullView categoryView = new CategoryFullView();
        categoryView.setCatPk(category.getCatPk());
        categoryView.setName(category.getName());
        categoryView.setDescription(category.getDescription());

        Page<ItemPartialView> itemPartialViews = mapItemsToItemPartialViews(items);
        categoryView.setItems(itemPartialViews);

        categoryView.setItemCount((int) items.getTotalElements());

        return categoryView;
    }

    public Page<ItemPartialView> mapItemsToItemPartialViews(Page<Item> items) {
        return items.map(this::toItemPartialView);
    }

    public abstract ItemPartialView toItemPartialView(Item item);
}
