package com.spring.storage.ItemManagement.api;

import com.spring.storage.CategoryManagement.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditItemRequest {

    private String name;

    private String category;

    private String description;

    private Boolean favorite;

    private Integer updateFrequency;

}