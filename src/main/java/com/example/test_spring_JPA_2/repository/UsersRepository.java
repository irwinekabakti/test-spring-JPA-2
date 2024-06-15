package com.example.test_spring_JPA_2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.test_spring_JPA_2.model.Users;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
//    boolean findByUsername(String username);
    Optional<Users> findByUsername(String username);
}
