package com.spring.storage.ItemManagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "An Item Partial View used to show basic information in a category item query")
public class ItemPartialView {

    private Long itemPk;

    private long version;

    private String name;

    private int amount;

    private boolean needsCheckup;

    private boolean favorite;

    private boolean needsShopping;

    private LocalDate lastModifiedAt;

    private String image;
}