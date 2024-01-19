package com.spring.storage.UserManagement.services;

import com.spring.storage.UserManagement.api.CreateUserRequest;
import com.spring.storage.UserManagement.api.UserView;
import com.spring.storage.UserManagement.model.User;

public interface UserService {

    Iterable<User> findAll();

    User findById(Long id);
    User findByUsername(String username);

    UserView createAdmin(CreateUserRequest createUserRequest);

    //User changePassword(String password);

    //User changeUsername(String username);
}
