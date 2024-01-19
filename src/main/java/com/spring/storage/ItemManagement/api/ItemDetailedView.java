package com.spring.storage.ItemManagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "An Item View for All its details")
public class ItemDetailedView {

    private Long itemPk;

    private String name;

    private int amount;

    private boolean favorite;

    private int updateFrequency;

    private boolean needsCheckup;

    private boolean needsShopping;

    private String description;

    private String categoryName;

    private Long categoryId;

    private LocalDate lastModifiedAt;

    private String image;
}