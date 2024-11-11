package com.example.backend.services;

import com.example.backend.dto.CreateUserDto;
import com.example.backend.dto.LoginUserDto;
import com.example.backend.dto.RecoveryJwtTokenDto;
import com.example.backend.entities.Role;
import com.example.backend.entities.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.security.authentication.JwtTokenService;
import com.example.backend.security.config.SecurityConfiguration;
import com.example.backend.security.userdetails.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private JwtTokenService jwtTokenService;

    public void createUser(CreateUserDto createUserDTO) {
        User newUser = User.builder()
                .name(createUserDTO.name())
                .password(securityConfiguration.passwordEncoder().encode(createUserDTO.password()))
                .roles(List.of(Role.builder().name(createUserDTO.role()).build()))
                .build();

        userRepository.save(newUser);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.name(), loginUserDto.password());

        Authentication authentication =  authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getLoggedUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        try {
            String username = jwtTokenService.getSubjectFromToken(token);
            return userRepository.findByName(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired token", e);
        }
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }
}
