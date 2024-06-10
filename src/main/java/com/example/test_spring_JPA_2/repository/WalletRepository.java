package com.example.test_spring_JPA_2.repository;
import com.example.test_spring_JPA_2.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUserIdAndIsMainTrue(Long userId);
    @Query("SELECT w FROM Wallet w ORDER BY w.updatedAt DESC")
    List<Wallet> findAllByOrderByUpdatedAtDesc();
}