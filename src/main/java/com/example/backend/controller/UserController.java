package com.example.backend.controller;

import com.example.backend.dto.LoginUserDto;
import com.example.backend.dto.RecoveryJwtTokenDto;
import com.example.backend.dto.RequestResponseDto;
import com.example.backend.dto.UserResponseDto;
import com.example.backend.entities.User;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("/*")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<RecoveryJwtTokenDto> login(@RequestBody LoginUserDto loginUserDto) {
        RecoveryJwtTokenDto token = userService.authenticateUser(loginUserDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/recoveryUser")
    public ResponseEntity<UserResponseDto> getUser(@RequestHeader("Authorization") String authorizationHeader) {
        User user = userService.getLoggedUser(authorizationHeader);
        return new ResponseEntity<>(UserResponseDto.fromEntity(user) , HttpStatus.OK);
    }

}
