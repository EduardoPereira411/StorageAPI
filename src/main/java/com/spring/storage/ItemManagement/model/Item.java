package com.spring.storage.ItemManagement.model;



import com.spring.storage.CategoryManagement.model.Category;
import com.spring.storage.Exceptions.ConflictException;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.StaleObjectStateException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Item {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemPk;

    @Version
    @Getter
    private long version;

    @Getter
    @Setter
    private LocalDate lastModifiedAt;

    @Column(nullable = false, unique = true)
    @NotNull
    @NotBlank
    @Size(min = 1, max = 32)
    @Getter
    @Setter
    private String name;

    @NotNull
    @Getter
    private int amount;

    @Getter
    @Setter
    private boolean favorite;

    @Getter
    private int updateFrequency;

    @Getter
    @Setter
    private boolean needsShopping;

    @Getter
    @Setter
    private boolean needsCheckup;

    @Size(min = 0, max = 2048)
    @Getter
    @Setter
    private String description;

    @Size(min = 0, max = 500)
    @Getter
    @Setter
    private String image;

    @ManyToOne
    @Getter
    @Setter
    private Category category;

    public Item(){
        setLastModifiedAt(LocalDate.now());
    }

    public Item(final String name, final Category category){
        setName(name);
        setCategory(category);
        setLastModifiedAt(LocalDate.now());
    }

    public Item(final String name, final String description,final Category category){
        setName(name);
        setDescription(description);
        setCategory(category);
        setLastModifiedAt(LocalDate.now());
    }

    public Item(final String name, final Integer updateFrequency,final Category category){
        setName(name);
        if(updateFrequency!=null){
            setUpdateFrequency(updateFrequency);
        }
        setCategory(category);
        setLastModifiedAt(LocalDate.now());
    }

    public Item(final String name, final Integer updateFrequency,final Category category, final String description){
        setName(name);
        if(updateFrequency!=null){
            setUpdateFrequency(updateFrequency);
        }
        setDescription(description);
        setCategory(category);
        setLastModifiedAt(LocalDate.now());
    }

    public void setAmount(final long desiredVersion, final int amount) {
        if (this.version != desiredVersion) {
            throw new ConflictException("Object was modified by another user");
        }
        if(amount>=0) {
            this.amount = amount;
            setLastModifiedAt(LocalDate.now());
        }else{
            throw new DataIntegrityViolationException("The amount cannot be below '0' ");
        }
    }

    public void setUpdateFrequency(final Integer updateFrequency){
        if(updateFrequency!=null) {
            if (updateFrequency < 0) {
                throw new DataIntegrityViolationException("The update frequency cannot be below '0' ");
            }
            this.updateFrequency= updateFrequency;
        }

    }

    public void toggleFavorite(final long desiredVersion) {
        if (this.version != desiredVersion) {
            throw new ConflictException("Object was modified by another user");
        }
        setFavorite(!isFavorite());
        updateShopping();
    }

    public void toggleShopping(final long desiredVersion) {
        if (this.version != desiredVersion) {
            throw new ConflictException("Object was modified by another user");
        }
        setNeedsShopping(!isNeedsShopping());
    }

    public void updateItem(final long desiredVersion, final String name, final String description, final Category category, final Boolean favorite, final Integer updateFrequency){
        if (this.version != desiredVersion) {
            throw new ConflictException("Object was modified by another user");
        }
        if(name!=null){
            setName(name);
        }
        if(description!=null){
            setDescription(description);
        }
        if(category!=null){
            setCategory(category);
        }
        if(favorite!=null) {
            setFavorite(favorite);
            updateShopping();
        }
        if(updateFrequency!=null){
            setUpdateFrequency(updateFrequency);
        }
    }

    public void updateImage(final long desiredVersion, final String imageURI){
        if (this.version != desiredVersion) {
            throw new ConflictException("Object was modified by another user");
        }
        if(imageURI!=null){
            setImage(imageURI);
        }
    }

    public void addAmount(final long desiredVersion,final Integer amount){
        if (this.version != desiredVersion) {
            throw new ConflictException("Object was modified by another user");
        }
        if (amount !=null){
            int i = getAmount()+amount;
            setAmount(desiredVersion,i);
            setLastModifiedAt(LocalDate.now());
            updateShopping();
        }
    }

    public void updateShopping(){
        if (favorite && amount == 0) {
            needsShopping = true;
        }
    }

    public void refreshLastModified(){
        setLastModifiedAt(LocalDate.now());
    }

    public boolean needsCheckup() {
        if(updateFrequency !=0) {
            LocalDate today = LocalDate.now();
            return ChronoUnit.DAYS.between(lastModifiedAt, today) >= updateFrequency;
        }
        return false;
    }

    public void editUpdateFrequency(final long desiredVersion,final Integer updateFrequency){
        if (this.version != desiredVersion) {
            throw new ConflictException("Object was modified by another user");
        }
        if(updateFrequency!=null) {
            setUpdateFrequency(updateFrequency);
        }
    }
}
