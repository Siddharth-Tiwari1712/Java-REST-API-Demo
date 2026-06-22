package com.example.Java.REST.API.Demo.security;

public class JwtResponseDto {

    private String token;
    private String type;
    private long expiresIn;
    private String username;

    public JwtResponseDto() {
    }

    public JwtResponseDto(String token, String type, long expiresIn, String username) {
        this.token = token;
        this.type = type;
        this.expiresIn = expiresIn;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
