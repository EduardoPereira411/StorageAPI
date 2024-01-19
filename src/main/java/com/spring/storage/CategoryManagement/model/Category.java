package com.spring.storage.CategoryManagement.model;


import com.spring.storage.Exceptions.ConflictException;
import com.spring.storage.ItemManagement.model.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Category {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long catPk;

    @Version
    @Getter
    private long version;

    @Column(nullable = false, unique = true)
    @NotNull
    @NotBlank
    @Size(min = 1, max = 32)
    @Getter
    @Setter
    private String name;

    @Column(nullable = true)
    @Size(min = 0, max = 2048)
    @Getter
    @Setter
    private String description;

    @OneToMany(mappedBy = "category")
    @Getter
    private List<Item> items;

    public Category(){}

    public Category(String name, String description){
        setName(name);
        setDescription(description);
    }

    public void update(long desiredVersion,String name, String description){
        if (this.version != desiredVersion) {
            throw new ConflictException("Object was modified by another user");
        }
        if(name!=null){
            setName(name);
        }
        if(description!=null){
            setDescription(description);
        }
    }
}
