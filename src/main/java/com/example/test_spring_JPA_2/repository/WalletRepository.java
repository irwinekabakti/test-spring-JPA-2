package com.example.test_spring_JPA_2.repository;
import com.example.test_spring_JPA_2.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUserIdAndIsMainTrue(Long userId);
    List<Wallet> findAll();
    Wallet findDataById(Long id);
}