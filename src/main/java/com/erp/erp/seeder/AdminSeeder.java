package com.erp.erp.seeder;

import com.erp.erp.model.User;
import com.erp.erp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@gmail.com";
        // To log in with UserDetailsService which uses findByUsername, we will set
        // username to "admin" or email.
        // We will query by both to be safe.

        // We will use username "admin" as the user login requirement.
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .email(adminEmail)
                    .password(passwordEncoder.encode("admin"))
                    .role("ADMIN")
                    .build();
            userRepository.save(admin);
            System.out.println("Admin user seeded successfully: admin@gmail.com / admin");
        } else {
            System.out.println("Admin user already exists. Skipping seed.");
        }
    }
}
