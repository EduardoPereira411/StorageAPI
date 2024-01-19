package com.spring.storage.CategoryManagement.api;

import com.spring.storage.CategoryManagement.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "A Category")
public class CategoryView {

    private Long catPk;

    private String name;

    private String description;

    private int itemCount;
}
