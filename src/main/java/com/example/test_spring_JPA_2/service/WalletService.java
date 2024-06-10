package com.example.test_spring_JPA_2.service;
import com.example.test_spring_JPA_2.model.Wallet;
import com.example.test_spring_JPA_2.repository.WalletRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WalletService {

    private WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

//    @Transactional
    public Wallet createWallet(String name, Double amount, Long userId) {
        Wallet wallet = new Wallet();
        wallet.setName(name);
        wallet.setAmount(amount);
        wallet.setUserId(userId);
        return walletRepository.save(wallet);
    }

    public Wallet updateWallet(Long id, String name, Double amount) {
        Wallet wallet = walletRepository.findById(id).orElse(null);
        if (wallet != null) {
            wallet.setName(name);
            wallet.setAmount(amount);
            return walletRepository.save(wallet);
        } else {
            return null; // Wallet not found
        }
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

    public Wallet findById(Long id) {
        return walletRepository.findById(id).orElse(null);
    }

//    public List<Wallet> getAllWalletsSortedByUpdatedAtDesc() {
//        return walletRepository.findAll(Sort.by(Sort.Direction.DESC, "updated_at"));
//    }
}
