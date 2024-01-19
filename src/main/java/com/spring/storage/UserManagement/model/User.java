package com.spring.storage.UserManagement.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class User implements UserDetails {
    private static final long serialVersionUID = 1L;

    // database primary key
    @Id
    @GeneratedValue
    @Getter
    private Long id;

    // optimistic lock concurrency control
    @Version
    @Getter
    private Long version;

    @Setter
    @Getter
    private boolean enabled = true;

    @Column(unique = true, updatable = false, nullable = false)
    @Getter
    @NotNull
    @NotBlank
    private String username;

    @Column(nullable = false)
    @Getter
    @NotNull
    @NotBlank
    private String password;

    @ElementCollection
    @Getter
    private final Set<Role> authorities = new HashSet<>();

    protected User() {
    }

    public User(final String username, final String password) {
        setUsername(username);
        setPassword(password);
    }

    public static User newUserWithRole(final String username, final String password, final String role) {
        final var u = new User(username, password);
        u.addAuthority(new Role(role));
        return u;
    }

    public void setPassword(final String password) {
        this.password = Objects.requireNonNull(password);
    }

    public void addAuthority(final Role r) {
        authorities.add(r);
    }

    public void setUsername(final String username) {
        this.username = Objects.requireNonNull(username);
    }

    @Override
    public boolean isAccountNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
