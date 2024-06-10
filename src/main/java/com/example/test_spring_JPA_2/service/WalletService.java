package com.example.test_spring_JPA_2.service;
import com.example.test_spring_JPA_2.model.Wallet;
import com.example.test_spring_JPA_2.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WalletService {

//    @Autowired
    private WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public void createWallet(Long userId, String name, Double amount) {
        Wallet wallet = new Wallet();
        wallet.setName(name);
        wallet.setAmount(amount);
        wallet.setUserId(userId);
        wallet.setIsMain(false);

        walletRepository.save(wallet);
    }

    @Transactional
    public void setMainWallet(Long userId, Long walletId) {
        Optional<Wallet> currentMainWallet = walletRepository.findByUserIdAndIsMainTrue(userId);
        currentMainWallet.ifPresent(wallet -> {
//            wallet.setIsMain(false);
            walletRepository.save(wallet);
        });

        Wallet newMainWallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

//        newMainWallet.setIsMain(true);
        walletRepository.save(newMainWallet);
    }

    public Iterable<Wallet> getAllWallets() {
        return walletRepository.findAll();
    }

}
