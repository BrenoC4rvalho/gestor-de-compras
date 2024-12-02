package com.example.backend;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Teste {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "admin";
        String hashedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Hashed password: " + hashedPassword);
    }
}
