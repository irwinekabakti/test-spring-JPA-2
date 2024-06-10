package com.example.test_spring_JPA_2.service;
import com.example.test_spring_JPA_2.model.Wallet;
import com.example.test_spring_JPA_2.repository.WalletRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {

    private WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createWallet(String name, Double amount, Long userId) {
        Wallet wallet = new Wallet();
        wallet.setName(name);
        wallet.setAmount(amount);
        wallet.setUserId(userId);
        return walletRepository.save(wallet);
    }

    /*
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
    */

    public Iterable<Wallet> getAllWallets() {
        return walletRepository.findAll();
//        return walletRepository.findAllByOrderByUpdatedAtDesc();
    }

    public Wallet findById(Long id) {
        return walletRepository.findById(id).orElse(null);
    }

    public Wallet updateWallet(Long id, String name, Double amount) {
        Wallet wallet = walletRepository.findById(id).orElse(null);
        if (wallet != null) {
            wallet.setName(name);
            wallet.setAmount(amount);
            return walletRepository.save(wallet);
        } else {
            return null;
        }
    }

    public Wallet deleteById(Long id) {
        Optional<Wallet> optionalWallet = walletRepository.findById(id);
        if (optionalWallet.isPresent()) {
            walletRepository.deleteById(id);
            return optionalWallet.get();
        } else {
            return null;
        }
    }
}
