package com.example.test_spring_JPA_2.service;

import java.util.HashSet;
import java.util.Set;

import com.example.test_spring_JPA_2.repository.AuthRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.test_spring_JPA_2.model.Users;
import com.example.test_spring_JPA_2.model.Roles;
import com.example.test_spring_JPA_2.dto.LoginResponseDTO;
import com.example.test_spring_JPA_2.repository.RolesRepository;
import com.example.test_spring_JPA_2.repository.UsersRepository;

@Service
@Transactional
public class AuthenticationService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthRedisRepository authRedisRepository;

    public Users registerUser(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        Set<Roles> authorities = new HashSet<>();

        if (username.toLowerCase().contains("admin")) {
            var adminRole = rolesRepository.findByAuthority("ADMIN");
            if (adminRole.isPresent()) {
                authorities.add(adminRole.get());
            } else {
                var defaultAdminRole = new Roles("ADMIN");
                rolesRepository.save(defaultAdminRole);
                authorities.add(defaultAdminRole);
            }
        } else {
            var userRole = rolesRepository.findByAuthority("USER");
            if (userRole.isPresent()) {
                authorities.add(userRole.get());
            } else {
                var defaultUserRole = new Roles("USER");
                rolesRepository.save(defaultUserRole);
                authorities.add(defaultUserRole);
            }
        }

        return usersRepository.save(new Users(0, username, encodedPassword, authorities));
    }

    /*
    public LoginResponseDTO loginUser(String username, String password){

        try {
            String token = redisUtil.get(username);
            if (token != null) {
                return new LoginResponseDTO(usersRepository.findByUsername(username).get(), token);
            }

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            token = tokenService.generateJwt(auth);
            redisUtil.set(username, token);

            return new LoginResponseDTO(usersRepository.findByUsername(username).get(), token);

        } catch (AuthenticationException e) {
            return new LoginResponseDTO(null, "");
        }
    }

     */

    /*
    public LoginResponseDTO loginUser(String username, String password){

        try {
            String token = authRedisRepository.getJwtKey(username);
            if (token != null) {
                return new LoginResponseDTO(usersRepository.findByUsername(username).get(), token);
            }

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            token = tokenService.generateJwt(auth);

            authRedisRepository.saveJwtKey(username, token);

            return new LoginResponseDTO(usersRepository.findByUsername(username).get(), token);

        } catch (AuthenticationException e) {
            return new LoginResponseDTO(null, "");
        }
    }
    */

    public LoginResponseDTO loginUser(String username, String password){
        try {
            String token = authRedisRepository.getJwtKey(username);
            if (token != null) {
                return new LoginResponseDTO(usersRepository.findByUsername(username).get(), token);
            }

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            token = tokenService.generateJwt(auth);

            authRedisRepository.saveJwtKey(username, token);

            return new LoginResponseDTO(usersRepository.findByUsername(username).get(), token);

        } catch (AuthenticationException e) {
            return new LoginResponseDTO(null, "");
        }
    }
}
