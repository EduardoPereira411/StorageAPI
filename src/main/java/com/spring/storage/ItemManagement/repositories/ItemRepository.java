package com.spring.storage.ItemManagement.repositories;

import com.spring.storage.CategoryManagement.model.Category;
import com.spring.storage.ItemManagement.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Override
    Optional<Item> findById(Long id);

    @Query("SELECT i FROM Item i WHERE i.name = :name")
    Optional<Item>findByName(String name);

    @Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE LOWER(concat('%',:name, '%')) AND i.amount = 0")
    Page<Item> findByNameContainingIgnoreCaseAndOutOfStock(@Param("name") String name, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.category = :category")
    Page<Item> findByCategory(@Param("category") Category category, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Item i SET i.category = :newCategory WHERE i.category = :oldCategory")
    int migrateItemsCategory(@Param("oldCategory") Category oldCategory, @Param("newCategory") Category newCategory);

    @Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE LOWER(concat('%',:name, '%')) AND i.needsShopping = true")
    Page<Item> findByNameContainingIgnoreCaseAndNeedsShopping(String name, Pageable pageable);

    Page<Item> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Item> findByNameContainingIgnoreCaseAndCategoryCatPk(String name, Long catPk, Pageable pageable);

    Page<Item> findByNameContainingIgnoreCaseAndFavoriteTrue(String name, Pageable pageable);
    @Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE LOWER(concat('%',:name, '%')) AND i.needsCheckup = true")
    Page<Item> findByNameContainingIgnoreCaseAndNeedsCheckup(String name, Pageable pageable);
}
