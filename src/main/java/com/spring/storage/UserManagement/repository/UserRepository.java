package com.spring.storage.UserManagement.repository;

import com.spring.storage.UserManagement.model.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    Optional<User> findById(Long objectId);


    @Query("SELECT o FROM User o WHERE o.username = :username")
    Optional<User> findByUsername(String username);
}
