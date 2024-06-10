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
//    @PostMapping
//    public CustomResponse<Wallet> createWallet(@Valid @RequestBody WalletRequest request) {
//        Wallet wallet = walletService.createWallet(request.getName(), request.getAmount(), request.getUserId());
//        return new CustomResponse<>(HttpStatus.CREATED, "Wallet created successfully", wallet);
//    }
//
//    @PutMapping("/{walletId}")
//    public CustomResponse<Wallet> updateWallet(@PathVariable Long walletId, @Valid @RequestBody WalletRequest request) {
//        Wallet updatedWallet = walletService.updateWallet(walletId, request.getName(), request.getAmount());
//        if (updatedWallet != null) {
//            return new CustomResponse<>(HttpStatus.OK, "Wallet updated successfully", updatedWallet);
//        } else {
//            return new CustomResponse<>(HttpStatus.NOT_FOUND, "Wallet not found with ID: " + walletId);
//        }
//    }

//    @PutMapping("/{walletId}/set-main")
//    public ResponseEntity<String> setMainWallet(@PathVariable Long walletId, @RequestParam Long userId) {
//        walletService.setMainWallet(userId, walletId);
//        return ResponseEntity.ok("Wallet set as main successfully");
//    }

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
}
