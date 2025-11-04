package com.hms.hospital_management_system.config;

import com.hms.hospital_management_system.models.User;
import com.hms.hospital_management_system.models.Role;
import com.hms.hospital_management_system.jpaRepository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println("🚀 DataInitializer is running...");
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123")); // encode password
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                System.out.println("\n\n\n\n\n✅ Admin user created with username=admin, password=admin123\n\n\n\n\n");
            }
        };
    }
}
