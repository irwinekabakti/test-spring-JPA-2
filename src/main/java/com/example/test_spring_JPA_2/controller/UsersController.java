package com.example.test_spring_JPA_2.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UsersController {

    @GetMapping("/")
    public String getUser(){
        return "User login success";
    }
}
