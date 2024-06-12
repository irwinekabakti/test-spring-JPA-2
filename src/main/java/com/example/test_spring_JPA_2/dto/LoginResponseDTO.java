package com.example.test_spring_JPA_2.dto;

import com.example.test_spring_JPA_2.model.Users;

public class LoginResponseDTO {
    private Users user;
    private String jwt;

    public LoginResponseDTO(){
        super();
    }

    public LoginResponseDTO(Users user, String jwt){
        this.user = user;
        this.jwt = jwt;
    }

    public Users getUser(){
        return this.user;
    }

    public void setUser(Users user){
        this.user = user;
    }

    public String getJwt(){
        return this.jwt;
    }

    public void setJwt(String jwt){
        this.jwt = jwt;
    }

}
