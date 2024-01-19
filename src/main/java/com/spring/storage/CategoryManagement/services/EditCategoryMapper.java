package com.spring.storage.CategoryManagement.services;

import com.spring.storage.CategoryManagement.api.CreateCategoryRequest;
import com.spring.storage.CategoryManagement.api.EditCategoryRequest;
import com.spring.storage.CategoryManagement.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class EditCategoryMapper {

    public abstract Category create(CreateCategoryRequest createCategoryRequest);

    public abstract Category update(EditCategoryRequest editCategoryRequest);
}
