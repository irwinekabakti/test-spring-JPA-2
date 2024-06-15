package com.example.test_spring_JPA_2.controller;

import com.example.test_spring_JPA_2.helper.Claims;
import com.example.test_spring_JPA_2.repository.BlacklistAuthRedisRepository;
import com.example.test_spring_JPA_2.util.CustomResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.test_spring_JPA_2.model.Users;
import com.example.test_spring_JPA_2.dto.LoginResponseDTO;
import com.example.test_spring_JPA_2.dto.RegistrationDTO;
import com.example.test_spring_JPA_2.service.AuthenticationService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    private final BlacklistAuthRedisRepository blacklistAuthRedisRepository;

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
    public ResponseEntity<CustomResponse<LoginResponseDTO>> loginUser(@RequestBody RegistrationDTO body, HttpServletResponse response) {
        try {
            LoginResponseDTO loginResponse = authenticationService.loginUser(body.getUsername(), body.getPassword());

            if (loginResponse.getJwt() != null) {
                Cookie cookie = new Cookie("jwt", loginResponse.getJwt());
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60);
                response.addCookie(cookie);
            }

            CustomResponse<LoginResponseDTO> customResponse = new CustomResponse<>(
                    HttpStatus.CREATED,
                    "Success",
                    "Login successful",
                    loginResponse
            );
            return customResponse.toResponseEntity();
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

    @Autowired
    public AuthenticationController(BlacklistAuthRedisRepository blacklistAuthRedisRepository) {
        this.blacklistAuthRedisRepository = blacklistAuthRedisRepository;
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String token = extractJwtFromCookies(request);

        if (token != null) {
            blacklistAuthRedisRepository.blacklistToken(token);
        }

        SecurityContextHolder.clearContext();

        // Clear the JWT cookie
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/profile")
    public Map<String, Object> getProfile() {
        return Claims.getClaimsFromJwt();
    }

    private String extractJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
