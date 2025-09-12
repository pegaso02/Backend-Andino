package com.andino.backend_pos.DTOs.ResponseDTOs;

public class LoginResponse {

    private String token;
    private String username;
    private String role;

    public LoginResponse(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }

}
