package com.example.test_spring_JPA_2.controller;
import com.example.test_spring_JPA_2.service.WalletService;
import com.example.test_spring_JPA_2.model.Wallet;
import com.example.test_spring_JPA_2.util.CustomResponse;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import com.example.test_spring_JPA_2.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
@Validated
public class WalletController {
    private WalletService walletService;
    public WalletController (WalletService walletService){
        this.walletService = walletService;
    }

    @PostMapping("/wallets")
    public ResponseEntity<CustomResponse<Wallet>> createWallet(@Valid @RequestBody Wallet wallet) {
        Wallet postWallet = walletService.createWallet(wallet.getName(), wallet.getAmount(), wallet.getUserId());
        if (postWallet != null) {
            CustomResponse<Wallet> response = new CustomResponse<>(HttpStatus.CREATED, "success", "New wallet created successfully", postWallet);
            return response.toResponseEntity();
        } else {
            CustomResponse<Wallet> response = new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "error", "Failed to create wallet", null);
            return response.toResponseEntity();
        }
    }

    @GetMapping("/wallets")
    public ResponseEntity<CustomResponse<Iterable<Wallet>>> getAllWallets() {
        Iterable<Wallet> wallets = walletService.getAllWallets();
        CustomResponse<Iterable<Wallet>> response = new CustomResponse<>(HttpStatus.OK, "Success", "All wallets retrieved successfully", wallets);
        return response.toResponseEntity();
    }

    @GetMapping("/wallet/{id}")
    public ResponseEntity<CustomResponse<Wallet>> getWalletById(@PathVariable Long id) {
        Wallet wallet = walletService.findById(id);
        if (wallet != null) {
            String walletName = wallet.getName();
            CustomResponse<Wallet> response = new CustomResponse<>(HttpStatus.OK, "success", walletName + " Wallet retrieved successfully", wallet);
            return response.toResponseEntity();
        } else {
            CustomResponse<Wallet> response = new CustomResponse<>(HttpStatus.NOT_FOUND, "error", "Wallet not found", null);
            return response.toResponseEntity();
        }
    }

    @PutMapping("/wallet/{id}")
    public ResponseEntity<CustomResponse<Wallet>> updateWallet(@PathVariable Long id, @Valid @RequestBody Wallet wallet) {
        Wallet updatedWallet = walletService.updateWallet(id, wallet.getName(), wallet.getAmount());
        if (updatedWallet != null) {
            String walletName = updatedWallet.getName();
            CustomResponse<Wallet> response = new CustomResponse<>(HttpStatus.OK, "success", walletName + " Wallet updated successfully", updatedWallet);
            return response.toResponseEntity();
        } else {
            CustomResponse<Wallet> response = new CustomResponse<>(HttpStatus.NOT_FOUND, "error", "Wallet not found", null);
            return response.toResponseEntity();
        }
    }

    @DeleteMapping("/wallet/{id}")
    public ResponseEntity<CustomResponse<Wallet>> deleteWallet(@PathVariable Long id) {
        Wallet deletedWallet = walletService.deleteById(id);
        if (deletedWallet != null) {
            CustomResponse<Wallet> response = new CustomResponse<>(HttpStatus.OK, "success", deletedWallet.getName() + " Wallet deleted successfully", deletedWallet);
            return response.toResponseEntity();
        } else {
            CustomResponse<Wallet> response = new CustomResponse<>(HttpStatus.NOT_FOUND, "error", "Wallet not found", null);
            return response.toResponseEntity();
        }
    }

    @GetMapping("/wallet/{id}/summary")
    public ResponseEntity<CustomResponse<Map<String, Map<String, String>>>> getWalletSummary(@PathVariable Long id) {
        Optional<Wallet> walletOptional = walletService.getWalletById(id);

        if (walletOptional.isEmpty()) {
            CustomResponse<Map<String, Map<String, String>>> response = new CustomResponse<>(HttpStatus.NOT_FOUND, "Wallet not found");
            return response.toResponseEntity();
        }

        Wallet wallet = walletOptional.get();
        Map<String, Map<String, String>> summary = walletService.getWalletSummary(id);
        String getMessage = wallet.getName() + " Wallet summary retrieved successfully";

        CustomResponse<Map<String, Map<String, String>>> response = new CustomResponse<>(HttpStatus.OK, "OK", getMessage, summary);
        return response.toResponseEntity();
    }

    @GetMapping("/wallets/transactions")
    public ResponseEntity<CustomResponse<Map<String, Object>>> getTransactions(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        Page<Transaction> transactions;

        try {
            if (startDate != null && endDate != null) {
                transactions = walletService.getTransactionsWithinDateRange(startDate, endDate, page, size);
            } else {
                transactions = walletService.getLatestTransactions(page, size);
            }

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("transactions", transactions.getContent());
            responseData.put("currentPage", transactions.getNumber());
            responseData.put("totalItems", transactions.getTotalElements());
            responseData.put("totalPages", transactions.getTotalPages());
            responseData.put("pageSize", transactions.getSize());

            CustomResponse<Map<String, Object>> customResponse = new CustomResponse<>(
                    HttpStatus.OK, "Success", "Transactions retrieved successfully", responseData
            );
            return customResponse.toResponseEntity();

        } catch (IllegalArgumentException e) {
            CustomResponse<Map<String, Object>> customResponse = new CustomResponse<>(
                    HttpStatus.BAD_REQUEST, "Bad Request", e.getMessage(), null
            );
            return customResponse.toResponseEntity();
        } catch (Exception e) {
            CustomResponse<Map<String, Object>> customResponse = new CustomResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred.", null
            );
            return customResponse.toResponseEntity();
        }
    }
}
