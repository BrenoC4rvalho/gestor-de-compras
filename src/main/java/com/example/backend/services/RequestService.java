package com.example.backend.services;

import com.example.backend.dto.CreateRequestDto;
import com.example.backend.dto.SignatureRequestDto;
import com.example.backend.entities.Request;
import com.example.backend.entities.User;
import com.example.backend.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    public Request create(CreateRequestDto createRequestDto, User requester) {
        Request newRequest = Request.builder()
                .title(createRequestDto.title())
                .description(createRequestDto.description())
                .price(createRequestDto.price())
                .maxSignatureDate(createRequestDto.maxSignatureDate())
                .requester(requester)
                .build();
        requestRepository.save(newRequest);
        return newRequest;
    }

    public Optional<Request> findById(Long id) {
        return requestRepository.findById(id);
    }

    public void delete(Long id) {
        requestRepository.deleteById(id);
    }

    public List<Request> findAll() {
        return requestRepository.findAll();
    }

    public Request update(Long id, CreateRequestDto createRequestDto) {
        Optional<Request> existingRequest = requestRepository.findById(id);

        Request request = existingRequest.get();

        request.setTitle(createRequestDto.title());
        request.setDescription(createRequestDto.description());
        request.setPrice(createRequestDto.price());
        request.setMaxSignatureDate(createRequestDto.maxSignatureDate());

        requestRepository.save(request);

        return request;
    }

    public Optional<Request> signature(Long id, User approver, SignatureRequestDto signatureRequestDto) {
        Optional<Request> existingRequest = requestRepository.findById(id);

        if(existingRequest.isEmpty()) {
            return Optional.empty();
        }
        Request request = existingRequest.get();
        request.setSignature(signatureRequestDto.signature());
        request.setApprover(approver);
        request.setSignatureDate(new Date());
        requestRepository.save(request);
        return Optional.of(request);
    }
}
