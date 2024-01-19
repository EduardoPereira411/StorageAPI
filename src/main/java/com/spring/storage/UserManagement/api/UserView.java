package com.spring.storage.UserManagement.api;

import com.spring.storage.UserManagement.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserView {
    private String id;

    private String username;

    private Set<Role> authorities;
}
