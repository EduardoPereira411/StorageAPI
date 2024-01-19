package com.spring.storage.Bootstraping;

import com.spring.storage.UserManagement.model.Role;
import com.spring.storage.UserManagement.model.User;
import com.spring.storage.UserManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
@Profile("bootstrap")
public class UserBootstrap implements CommandLineRunner {

    private final UserRepository userRepo;

    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public void run(final String... args) throws Exception {
        // admin
        if (userRepo.findByUsername("AdminUser").isEmpty()) {
            final User u1 = User.newUserWithRole("AdminUser", encoder.encode("Password1"), Role.ADMIN);
            userRepo.save(u1);
        }
    }
}