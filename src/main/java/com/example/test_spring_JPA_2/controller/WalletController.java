package com.example.test_spring_JPA_2.controller;
import com.example.test_spring_JPA_2.service.WalletService;
import com.example.test_spring_JPA_2.model.Wallet;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/wallet")
@Validated
public class WalletController {
    private WalletService walletService;
    public WalletController (WalletService walletService){
        this.walletService = walletService;
    }


    @PostMapping
    public ResponseEntity<String> createWallet(@RequestBody @Valid Wallet walletRequest) {
        walletService.createWallet(walletRequest.getUserId(), walletRequest.getName(), walletRequest.getAmount());
        return ResponseEntity.ok("Wallet created successfully");
    }

    @PutMapping("/{walletId}/set-main")
    public ResponseEntity<String> setMainWallet(@PathVariable Long walletId, @RequestParam Long userId) {
        walletService.setMainWallet(userId, walletId);
        return ResponseEntity.ok("Wallet set as main successfully");
    }

    @GetMapping("/all-wallets")
    public ResponseEntity<Iterable<Wallet>> getAllWallets() {
        Iterable<Wallet> wallets = walletService.getAllWallets();
        return ResponseEntity.ok(wallets);
    }
}
