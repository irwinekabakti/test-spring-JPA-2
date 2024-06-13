package com.example.test_spring_JPA_2.service;

import com.example.test_spring_JPA_2.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.test_spring_JPA_2.repository.UsersRepository;

@Service
public class UsersService implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("In the user details service");

        return usersRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user is not valid"));
    }

//    public UserDetails findUserByUsername(String username) throws UsernameNotFoundException {
//        return usersRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user is not valid"));
//    }
}
