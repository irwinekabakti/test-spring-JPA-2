package com.example.test_spring_JPA_2.controller;

import com.example.test_spring_JPA_2.util.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.test_spring_JPA_2.model.Users;
import com.example.test_spring_JPA_2.dto.LoginResponseDTO;
import com.example.test_spring_JPA_2.dto.RegistrationDTO;
import com.example.test_spring_JPA_2.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<CustomResponse<Users>> registerUser(@RequestBody RegistrationDTO body){
        Users user = authenticationService.registerUser(body.getUsername(), body.getPassword());
        CustomResponse<Users> response = new CustomResponse<>(
                HttpStatus.CREATED,
                "Success",
                "User registered successfully",
                user
        );

        return response.toResponseEntity();
    }

    @PostMapping("/login")

    public ResponseEntity<CustomResponse<LoginResponseDTO>> loginUser(@RequestBody RegistrationDTO body){
        try {
            LoginResponseDTO loginResponse = authenticationService.loginUser(body.getUsername(), body.getPassword());
            CustomResponse<LoginResponseDTO> response = new CustomResponse<>(
                    HttpStatus.CREATED,
                    "Success",
                    "Login successful",
                    loginResponse
            );
            return response.toResponseEntity();
        } catch (RedisConnectionFailureException e) {
            CustomResponse<LoginResponseDTO> errorResponse = new CustomResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error",
                    "Failed to connect to Redis",
                    null
            );
            return errorResponse.toResponseEntity();
        } catch (Exception e) {
            CustomResponse<LoginResponseDTO> errorResponse = new CustomResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error",
                    "An unexpected error occurred",
                    null
            );
            return errorResponse.toResponseEntity();
        }
    }
}
