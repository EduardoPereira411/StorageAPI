package com.spring.storage.CategoryManagement.api;

import com.spring.storage.ItemManagement.api.ItemPartialView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Schema(description = "A Category Full View")
public class CategoryFullView {

    private Long catPk;

    private String name;

    private String description;

    private int itemCount;

    private Page<ItemPartialView> items;

}
