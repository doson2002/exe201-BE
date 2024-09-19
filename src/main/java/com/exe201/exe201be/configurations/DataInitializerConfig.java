package com.exe201.exe201be.configurations;


import com.exe201.exe201be.entities.Role;
import com.exe201.exe201be.entities.Users;
import com.exe201.exe201be.repositories.RoleRepository;
import com.exe201.exe201be.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializerConfig {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            initializeRole("Admin", roleRepository);
            initializeRole("Partner", roleRepository);
            initializeRole("Customer", roleRepository);

            userRepository.findByEmail("admin2@gmail.com").orElseGet(() -> {
                Users adminUser = new Users();
                Role adminRole = roleRepository.findByName("Admin").orElseThrow(() -> new RuntimeException("Role 'Admin' not found"));
                adminUser.setEmail("admin2@gmail.com");
                adminUser.setPassword(passwordEncoder.encode("123456")); // Consider using encoded passwords in production
                adminUser.setActive(true);
                adminUser.setFullName("admin 1");
                adminUser.setPhoneNumber("123456789");
                adminUser.setFirstLogin(false);
                adminUser.setGender(1);
                adminUser.setRole(adminRole);
                return userRepository.save(adminUser);
            });
        };
    }
    private void initializeRole(String roleName, RoleRepository roleRepository) {
        roleRepository.findByName(roleName).ifPresentOrElse(
                role -> {
                    // Role already exists, do nothing
                },
                () -> {
                    // Role does not exist, create a new one
                    Role newRole = new Role();
                    newRole.setName(roleName);
                    roleRepository.save(newRole);
                }
        );
    }
}