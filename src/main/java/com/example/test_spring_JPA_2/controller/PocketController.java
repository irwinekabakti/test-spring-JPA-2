package com.example.test_spring_JPA_2.controller;
import com.example.test_spring_JPA_2.dto.PocketDTO;
import com.example.test_spring_JPA_2.model.Pocket;
import com.example.test_spring_JPA_2.service.PocketService;
import com.example.test_spring_JPA_2.util.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class PocketController {
    private final PocketService pocketService;

    @Autowired
    public PocketController(PocketService pocketService) {
        this.pocketService = pocketService;
    }

    @PostMapping("/pockets")
    public ResponseEntity<CustomResponse<Pocket>> createPocket(@RequestBody Pocket pocket) {
        Pocket createdPocket = pocketService.createPocket(pocket);
        CustomResponse<Pocket> response = new CustomResponse<>(HttpStatus.CREATED, "Success", "Pocket created successfully", createdPocket);

        return response.toResponseEntity();
    }

    @GetMapping("/pockets")
    public ResponseEntity<CustomResponse<List<PocketDTO>>> getAllPockets() {
        List<PocketDTO> pockets = pocketService.getAllPockets();
        CustomResponse<List<PocketDTO>> response = new CustomResponse<>(HttpStatus.OK, "Success", "Pockets retrieved successfully", pockets);
        return response.toResponseEntity();
    }

    @GetMapping("/pocket/{id}")
    public ResponseEntity<CustomResponse<PocketDTO>> getPocketById(@PathVariable Long id) {
        Optional<PocketDTO> optionalPocket = pocketService.getPocketById(id);
        if (optionalPocket.isPresent()) {
            CustomResponse<PocketDTO> response = new CustomResponse<>(HttpStatus.OK, "Success", "Pocket retrieved successfully", optionalPocket.get());
            return response.toResponseEntity();
        } else {
            CustomResponse<PocketDTO> response = new CustomResponse<>(HttpStatus.NOT_FOUND, "Error", "Pocket not found", null);
            return response.toResponseEntity();
        }
    }

    @PutMapping("/pocket/{id}")
    public ResponseEntity<CustomResponse<PocketDTO>> updatePocket(@PathVariable Long id, @RequestBody PocketDTO updatedPocketDTO) {
        try {
            Pocket pocket = pocketService.updatePocket(id, updatedPocketDTO);
            PocketDTO newUpdatedPocketDTO = pocketService.convertToDTO(pocket);
            CustomResponse<PocketDTO> response = new CustomResponse<>(HttpStatus.OK, "Success", newUpdatedPocketDTO.getName() + " Pocket updated successfully", newUpdatedPocketDTO);
            return response.toResponseEntity();
        } catch (IllegalArgumentException e) {
            CustomResponse<PocketDTO> response = new CustomResponse<>(HttpStatus.NOT_FOUND, "Error", e.getMessage(), null);
            return response.toResponseEntity();
        }
    }

    @DeleteMapping("/pocket/{id}")
    public ResponseEntity<CustomResponse<PocketDTO>> deletePocket(@PathVariable Long id) {
        try {
            PocketDTO deletedPocket = pocketService.deletePocket(id);
            CustomResponse<PocketDTO> response = new CustomResponse<>(HttpStatus.OK, "Success", deletedPocket.getName() + " Pocket deleted successfully", deletedPocket);
            return response.toResponseEntity();
        } catch (IllegalArgumentException e) {
            CustomResponse<PocketDTO> response = new CustomResponse<>(HttpStatus.NOT_FOUND, "Error", e.getMessage(), null);
            return response.toResponseEntity();
        }
    }
}