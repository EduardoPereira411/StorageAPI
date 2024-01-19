package com.spring.storage.ItemManagement.api;

import com.spring.storage.ItemManagement.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ItemViewMapper {

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "category.catPk", target = "categoryId")
    public abstract ItemDetailedView toItemDetailedView(Item item);

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "category.catPk", target = "categoryId")
    public abstract Iterable<ItemDetailedView> toItemDetailedView(Iterable<Item> items);

    @Mapping(source = "category.name", target = "categoryName")
    public abstract ItemView toItemView(Item item);

    @Mapping(source = "category.name", target = "categoryName")
    public abstract Iterable<ItemView> toItemView(Iterable<Item> items);

    public abstract ItemPartialView toItemPartialView(Item item);

    public abstract Iterable<ItemPartialView> toItemPartialView(Iterable<Item> items);

}
