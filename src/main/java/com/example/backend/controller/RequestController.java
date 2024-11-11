package com.example.backend.controller;

import com.example.backend.dto.CreateRequestDto;
import com.example.backend.dto.SignatureRequestDto;
import com.example.backend.entities.Request;
import com.example.backend.entities.User;
import com.example.backend.services.RequestService;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Optional<Request>> showRequest(@PathVariable Long id) {
        Optional<Request> request = requestService.findById(id);

        if (request.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Request> createRequest(@RequestHeader("Authorization") String authorizationHeader, @RequestBody CreateRequestDto createRequestDto) {
        User requester = userService.getLoggedUser(authorizationHeader);

        if(userService.findByName(requester.getName()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Request newRequest = requestService.create(createRequestDto, requester);
        return new ResponseEntity<>(newRequest, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Request> editRequest(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id, @RequestBody CreateRequestDto createRequestDto) {
        User loggedUser = userService.getLoggedUser(authorizationHeader);
        System.out.println();
        Optional<Request> request = requestService.findById(id);

        if (request.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Request existingRequest = request.get();

        if(existingRequest.getSignature() != null && !loggedUser.getRoles().contains("ROLE_ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if(!existingRequest.getRequester().equals(loggedUser) && !loggedUser.getRoles().contains("ROLE_ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Request editRequest = requestService.update(id, createRequestDto);
        return new ResponseEntity<>(editRequest, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
        User loggedUser = userService.getLoggedUser(authorizationHeader);
        Optional<Request> request = requestService.findById(id);

        if (request.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Request existingRequest = request.get();

        if (existingRequest.getSignature() != null && !loggedUser.getRoles().contains("ROLE_ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        requestService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Request>> showAllRequests() {
        List<Request> requests = requestService.findAll();

        if(requests.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @PatchMapping("/approver/{id}")
    public ResponseEntity<?> approverRequest(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader, @RequestBody SignatureRequestDto signatureRequestDto) {
        User loggedUser = userService.getLoggedUser(authorizationHeader);
        Optional<Request> optionalRequest = requestService.findById(id);

        if(optionalRequest.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Request request = optionalRequest.get();

        if(request.getRequester().getId().equals(loggedUser.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<Request> requestEdit = requestService.signature(id, loggedUser, signatureRequestDto);
        return new ResponseEntity<>(requestEdit, HttpStatus.OK);

    }

}