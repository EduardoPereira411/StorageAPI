package com.spring.storage.UserManagement.services;

import com.spring.storage.UserManagement.api.CreateUserRequest;
import com.spring.storage.UserManagement.model.User;
import com.spring.storage.UserManagement.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class EditUserMapper {


    private UserRepository userRepository;

    //@Mapping(source = "authorities", target = "authorities", qualifiedByName = "stringToRole")
    public abstract User createAdmin(CreateUserRequest request);
}
