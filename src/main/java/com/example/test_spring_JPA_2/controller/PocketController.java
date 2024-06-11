package com.example.test_spring_JPA_2.controller;

import com.example.test_spring_JPA_2.model.Pocket;
import com.example.test_spring_JPA_2.service.PocketService;
import com.example.test_spring_JPA_2.util.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        CustomResponse<Pocket> response = new CustomResponse<>(HttpStatus.CREATED, "success", "Pocket created successfully", createdPocket);

        return response.toResponseEntity();
    }
}