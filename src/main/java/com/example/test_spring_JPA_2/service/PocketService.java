package com.example.test_spring_JPA_2.service;

import com.example.test_spring_JPA_2.model.Pocket;
import com.example.test_spring_JPA_2.model.Wallet;
import com.example.test_spring_JPA_2.repository.PocketRepository;
import com.example.test_spring_JPA_2.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PocketService {

    @Autowired
    private PocketRepository pocketRepository;

    @Autowired
    private WalletRepository walletRepository;

    public PocketService(PocketRepository pocketRepository) {
        this.pocketRepository = pocketRepository;
    }

    public Pocket createPocket(Pocket pocket) {
        // Perform any additional business logic or validation here
        return pocketRepository.save(pocket);
    }
}
