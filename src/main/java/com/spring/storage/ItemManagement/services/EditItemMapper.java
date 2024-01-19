package com.spring.storage.ItemManagement.services;

import com.spring.storage.CategoryManagement.model.Category;
import com.spring.storage.CategoryManagement.repositories.CategoryRepository;
import com.spring.storage.ItemManagement.api.CreateItemRequest;
import com.spring.storage.ItemManagement.model.Item;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ValidationException;

@Mapper(componentModel = "spring")
public abstract class EditItemMapper {

    @Autowired
    private CategoryRepository categoryRepository;

    public abstract Item create(CreateItemRequest createItemRequest);

    public Category toCategory(final String categoryName) {
        return categoryRepository.findByName(categoryName).orElseThrow(() -> new ValidationException("Select an existing category"));
    }

    public Category toCategory(final Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ValidationException("Select an existing category"));
    }
}
