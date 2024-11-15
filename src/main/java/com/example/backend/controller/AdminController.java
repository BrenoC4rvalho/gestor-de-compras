package com.example.backend.controller;

import com.example.backend.dto.CreateUserDto;
import com.example.backend.dto.UserResponseDto;
import com.example.backend.entities.User;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserDto createUserDTO) {
        userService.createUser(createUserDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserResponseDto>> showUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/user")
    public void editUser() {

    }

    @DeleteMapping("/user")
    public void deleteUser() {

    }

}
