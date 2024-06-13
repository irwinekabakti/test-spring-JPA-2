package com.example.test_spring_JPA_2.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.test_spring_JPA_2.model.Roles;
import com.example.test_spring_JPA_2.repository.RolesRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRoles(RolesRepository rolesRepository) {
        return args -> {
            if (rolesRepository.findByAuthority("USER").isEmpty()) {
                rolesRepository.save(new Roles("USER"));
            }
            if (rolesRepository.findByAuthority("ADMIN").isEmpty()) {
                rolesRepository.save(new Roles("ADMIN"));
            }
        };
    }
}