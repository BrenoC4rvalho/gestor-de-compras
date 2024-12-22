package com.example.backend.controller;

import com.example.backend.dto.CreateRequestDto;
import com.example.backend.dto.RequestResponseDto;
import com.example.backend.dto.SignatureRequestDto;
import com.example.backend.entities.Request;
import com.example.backend.entities.User;
import com.example.backend.services.RequestService;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/request")
public class RequestController {

    @Autowired
    private UserService userService;

    @Autowired
    private RequestService requestService;

    @GetMapping("/{id}")
    public ResponseEntity<Optional<RequestResponseDto>> showRequest(@PathVariable Long id) {
        Optional<RequestResponseDto> request = requestService.findById(id);

        if (request.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RequestResponseDto> createRequest(@RequestHeader("Authorization") String authorizationHeader, @RequestBody CreateRequestDto createRequestDto) {
        User requester = userService.getLoggedUser(authorizationHeader);

        RequestResponseDto newRequest = requestService.create(createRequestDto, requester);
        return new ResponseEntity<>(newRequest, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<RequestResponseDto> editRequest(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id,
            @RequestBody CreateRequestDto createRequestDto
    ) {
        User loggedUser = userService.getLoggedUser(authorizationHeader);
        Optional<RequestResponseDto> request = requestService.findById(id);

        if (request.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if(request.get().signature() != null && !loggedUser.getRoles().contains("ROLE_ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if(!request.get().requesterName().equals(loggedUser.getName()) && !loggedUser.getRoles().contains("ROLE_ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        RequestResponseDto editRequest = requestService.update(id, createRequestDto);
        return new ResponseEntity<>(editRequest, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
        User loggedUser = userService.getLoggedUser(authorizationHeader);
        Optional<RequestResponseDto> request = requestService.findById(id);

        if (request.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //if (request.get().signature() != null && !loggedUser.getRoles().contains("ROLE_ADMIN")) {
            //return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        //}

        requestService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<RequestResponseDto>> showAllRequests() {
        List<RequestResponseDto> requests = requestService.findAll();

        if(requests.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @PatchMapping("/approver/{id}")
    public ResponseEntity<Optional<RequestResponseDto>> approverRequest(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader, @RequestBody SignatureRequestDto signatureRequestDto) {
        System.out.println("Entrei na assinatura");
        User loggedUser = userService.getLoggedUser(authorizationHeader);
        Optional<RequestResponseDto> request = requestService.findById(id);

        if(request.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if(request.get().requesterName().equals(loggedUser.getName())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<RequestResponseDto> requestEdit = requestService.signature(id, loggedUser, signatureRequestDto);
        return new ResponseEntity<>(requestEdit, HttpStatus.OK);

    }

}
