package com.example.test_spring_JPA_2.dto;

public class PocketDTO {
    private Long id;
    private String name;
    private String emoji;
    private String description;
    private double budget;
    private double spent;
    private Long walletId;

    // Constructors, Getters and Setters
    public PocketDTO() {}

    public PocketDTO(Long id, String name, String emoji, String description, double budget, double spent, Long walletId) {
        this.id = id;
        this.name = name;
        this.emoji = emoji;
        this.description = description;
        this.budget = budget;
        this.spent = spent;
        this.walletId = walletId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getSpent() {
        return spent;
    }

    public void setSpent(double spent) {
        this.spent = spent;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }
}
