package com.example.test_spring_JPA_2.repository;
import com.example.test_spring_JPA_2.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByWalletId(Long walletId);
}
