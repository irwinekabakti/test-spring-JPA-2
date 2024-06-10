package com.example.test_spring_JPA_2.controller;
import com.example.test_spring_JPA_2.service.WalletService;
import com.example.test_spring_JPA_2.model.Wallet;
import com.example.test_spring_JPA_2.util.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/wallets")
@Validated
public class WalletController {
    private WalletService walletService;
    public WalletController (WalletService walletService){
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<CustomResponse<Wallet>> createWallet(@Valid @RequestBody Wallet wallet) {
//        try {
//            Wallet postWallet = walletService.createWallet(wallet.getName(), wallet.getAmount(), wallet.getUserId());
//            return ResponseEntity.status(HttpStatus.CREATED).body(new CustomResponse<>(HttpStatus.CREATED, "success", "New wallet created successfully", postWallet));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "error", "Failed to create wallet", null));
//        }

        Wallet postWallet = walletService.createWallet(wallet.getName(), wallet.getAmount(), wallet.getUserId());
        if (postWallet != null) {
            CustomResponse<Wallet> response = new CustomResponse<>(HttpStatus.CREATED, "success", "New wallet created successfully", postWallet);
            return response.toResponseEntity();
        } else {
            CustomResponse<Wallet> response = new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "error", "Failed to create wallet", null);
            return response.toResponseEntity();
        }
    }

    @GetMapping
    public ResponseEntity<CustomResponse<Iterable<Wallet>>> getAllWallets() {
        Iterable<Wallet> wallets = walletService.getAllWallets();
        CustomResponse<Iterable<Wallet>> response = new CustomResponse<>(HttpStatus.OK, "Success", "All wallets retrieved successfully", wallets);
        return response.toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<Wallet>> getWalletById(@PathVariable Long id) {
        Wallet wallet = walletService.findById(id);
        if (wallet != null) {
            String walletName = wallet.getName();
            return ResponseEntity.ok(new CustomResponse<>(HttpStatus.OK, "success", walletName  + " Wallet retrieved successfully", wallet));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<Wallet>> updateWallet(@PathVariable Long id, @Valid @RequestBody Wallet wallet) {
        Wallet updatedWallet = walletService.updateWallet(id, wallet.getName(), wallet.getAmount());
        if (updatedWallet != null) {
            String walletName = wallet.getName();
            return ResponseEntity.ok(new CustomResponse<>(HttpStatus.OK, "success", walletName + " Wallet updated successfully", updatedWallet));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Wallet>> deleteWallet(@PathVariable Long id) {
        Wallet deletedWallet = walletService.deleteById(id);
        if (deletedWallet != null) {
            return ResponseEntity.ok(new CustomResponse<>(HttpStatus.OK, "success",  deletedWallet.getName()+ " Wallet deleted successfully", deletedWallet));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponse<>(HttpStatus.NOT_FOUND, "error", "Wallet not found", null));
        }
    }
}
