package com.example.test_spring_JPA_2.service;
import com.example.test_spring_JPA_2.dto.PocketDTO;
import com.example.test_spring_JPA_2.model.Pocket;
import com.example.test_spring_JPA_2.model.Wallet;
import com.example.test_spring_JPA_2.repository.PocketRepository;
import com.example.test_spring_JPA_2.repository.WalletRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PocketService {
    private final PocketRepository pocketRepository;
    private final WalletRepository walletRepository;

    public PocketService(PocketRepository pocketRepository, WalletRepository walletRepository) {
        this.pocketRepository = pocketRepository;
        this.walletRepository = walletRepository;
    }

    public PocketDTO createPocket(PocketDTO pocketDTO) {
        Wallet wallet = walletRepository.findById(pocketDTO.getWalletId())
                .orElseThrow(() -> new IllegalArgumentException("Wallet with id " + pocketDTO.getWalletId() + " not found"));

        Pocket pocket = new Pocket();
        pocket.setName(pocketDTO.getName());
        pocket.setEmoji(pocketDTO.getEmoji());
        pocket.setDescription(pocketDTO.getDescription());
        pocket.setBudget(pocketDTO.getBudget());
        pocket.setSpent(pocketDTO.getSpent());
        pocket.setWallet(wallet);

        Pocket savedPocket = pocketRepository.save(pocket);
        return convertToDTO(savedPocket);
    }

    public List<PocketDTO> getAllPockets() {
        List<Pocket> pockets = pocketRepository.findAll();
        return pockets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<PocketDTO> getPocketById(Long id) {
        return pocketRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Pocket updatePocket(Long id, PocketDTO updatedPocketDTO) {
        Optional<Pocket> optionalPocket = pocketRepository.findById(id);
        if (optionalPocket.isPresent()) {
            Pocket existingPocket = optionalPocket.get();
            existingPocket.setName(updatedPocketDTO.getName());
            existingPocket.setEmoji(updatedPocketDTO.getEmoji());
            existingPocket.setDescription(updatedPocketDTO.getDescription());
            existingPocket.setBudget(updatedPocketDTO.getBudget());
            existingPocket.setSpent(updatedPocketDTO.getSpent());

            // Handle wallet update
            if (updatedPocketDTO.getWalletId() != null) {
                Wallet newWallet = walletRepository.findById(updatedPocketDTO.getWalletId())
                        .orElseThrow(() -> new IllegalArgumentException("Wallet with id " + updatedPocketDTO.getWalletId() + " not found"));
                existingPocket.setWallet(newWallet);
            }

            return pocketRepository.save(existingPocket);
        } else {
            throw new IllegalArgumentException("Pocket with id " + id + " not found");
        }
    }

    public PocketDTO deletePocket(Long id) {
        Pocket pocket = pocketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pocket with id " + id + " not found"));
        pocketRepository.deleteById(id);
        return convertToDTO(pocket);
    }

    public PocketDTO convertToDTO(Pocket pocket) {
        Long walletId = pocket.getWallet() != null ? pocket.getWallet().getId() : null;
        return new PocketDTO(
                pocket.getId(),
                pocket.getName(),
                pocket.getEmoji(),
                pocket.getDescription(),
                pocket.getBudget(),
                pocket.getSpent(),
                walletId
        );
    }

}