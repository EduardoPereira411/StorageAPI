package com.spring.storage.UserManagement.services;

import com.spring.storage.Exceptions.NotFoundException;
import com.spring.storage.ItemManagement.model.Item;
import com.spring.storage.UserManagement.api.CreateUserRequest;
import com.spring.storage.UserManagement.api.UserView;
import com.spring.storage.UserManagement.api.UserViewMapper;
import com.spring.storage.UserManagement.model.User;
import com.spring.storage.UserManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    UserRepository userRepository;

    EditUserMapper editUserMapper;
    private final UserViewMapper userViewMapper;
    public Iterable<User> findAll(){
        return userRepository.findAll();
    }

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("There is no User with the specified Id"));
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("There is no User with the specified Username"));
    }

    public UserView createAdmin(CreateUserRequest createUserRequest){
        if (userRepository.findByUsername(createUserRequest.getUsername()).isPresent()) {
            throw new DataIntegrityViolationException("Username already exists!");
        }
        if (!createUserRequest.getPassword().equals(createUserRequest.getRePassword())) {
            throw new ValidationException("Passwords don't match!");
        }
        User user = editUserMapper.createAdmin(createUserRequest);

        user = userRepository.save(user);
        return userViewMapper.toUserView(user);
    }
}
