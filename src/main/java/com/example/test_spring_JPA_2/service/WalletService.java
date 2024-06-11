package com.example.test_spring_JPA_2.service;
import com.example.test_spring_JPA_2.model.Wallet;
import com.example.test_spring_JPA_2.model.Transaction;
import com.example.test_spring_JPA_2.repository.WalletRepository;
import com.example.test_spring_JPA_2.repository.TransactionRepository;
//import jakarta.transaction.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {

    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;

    public WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
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

//    private int getNumberOfExpensePockets(Long walletId) {
//        List<ExpensePocket> expensePockets = expensePocketRepository.findByWalletId(walletId);
//        return expensePockets.size();
//    }

    public Map<String, Map<String, String>> getWalletSummary(Long walletId) {
        Map<String, BigDecimal> incomeSummary = new HashMap<>();
        Map<String, BigDecimal> expenseSummary = new HashMap<>();
        List<Transaction> transactions = transactionRepository.findByWalletId(walletId);

        for (Transaction transaction : transactions) {
            if ("income".equals(transaction.getType())) {
                incomeSummary.merge(transaction.getCurrency(), transaction.getAmount(), BigDecimal::add);
            } else if ("expense".equals(transaction.getType())) {
                expenseSummary.merge(transaction.getCurrency(), transaction.getAmount(), BigDecimal::add);
            }
        }

        Map<String, String> formattedIncomeSummary = formatCurrencyMap(incomeSummary);
        Map<String, String> formattedExpenseSummary = formatCurrencyMap(expenseSummary);

        Map<String, Map<String, String>> summary = new HashMap<>();
        summary.put("totalIncome", formattedIncomeSummary);
        summary.put("totalExpenses", formattedExpenseSummary);
        return summary;
    }

    private Map<String, String> formatCurrencyMap(Map<String, BigDecimal> currencyMap) {
        Map<String, String> formattedMap = new HashMap<>();
        for (Map.Entry<String, BigDecimal> entry : currencyMap.entrySet()) {
            formattedMap.put(entry.getKey(), entry.getKey() + " " + entry.getValue().toString());
        }
        return formattedMap;
    }

    public Optional<Wallet> getWalletById(Long walletId) {
        return walletRepository.findById(walletId);
    }
    public BigDecimal calculateTotalIncome(Long walletId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = transactionRepository.findByWalletId(walletId);
        BigDecimal totalIncome = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            if ("income".equals(transaction.getType())) {
                totalIncome = totalIncome.add(transaction.getAmount());
            }
        }
        return totalIncome;
    }

    public BigDecimal calculateTotalExpenses(Long walletId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = transactionRepository.findByWalletId(walletId);
        BigDecimal totalExpenses = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            if ("expense".equals(transaction.getType())) {
                totalExpenses = totalExpenses.add(transaction.getAmount());
            }
        }
        return totalExpenses;
    }

    /*
    private int getNumberOfExpensePockets(Long walletId) {
        List<ExpensePocket> expensePockets = expensePocketRepository.findByWalletId(walletId);
        return expensePockets.size();
    }

    private int getNumberOfGoals(Long walletId) {
        List<Goal> goals = goalRepository.findByWalletId(walletId);
        return goals.size();
    }
    */
}
