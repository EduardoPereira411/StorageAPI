package com.spring.storage.CategoryManagement.services;

import com.spring.storage.CategoryManagement.api.CreateCategoryRequest;
import com.spring.storage.CategoryManagement.api.EditCategoryRequest;
import com.spring.storage.CategoryManagement.model.Category;
import com.spring.storage.ItemManagement.model.Item;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CategoryService {

    Page<Category> findAll(int page, String field, boolean ascending);

    Category findByName(String name);

    Category findById(Long id);

    Category create(CreateCategoryRequest createCategoryRequest);

    Category update (long desiredVersion, Long id, EditCategoryRequest editCategoryRequest);

    Page<Category> queryByName(String query, int page, String field, boolean ascending);
}
