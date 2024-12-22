package com.example.backend.services;

import com.example.backend.dto.CreateRequestDto;
import com.example.backend.dto.RequestResponseDto;
import com.example.backend.dto.SignatureRequestDto;
import com.example.backend.entities.Request;
import com.example.backend.entities.User;
import com.example.backend.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    public RequestResponseDto create(CreateRequestDto createRequestDto, User requester) {
        Request newRequest = Request.builder()
                .title(createRequestDto.title())
                .description(createRequestDto.description())
                .price(createRequestDto.price())
                .maxSignatureDate(createRequestDto.maxSignatureDate())
                .requester(requester)
                .build();
        requestRepository.save(newRequest);

        return RequestResponseDto.fromEntity(newRequest);
    }

    public Optional<RequestResponseDto> findById(Long id) {
        return requestRepository.findById(id)
                .map(RequestResponseDto::fromEntity);
    }

    public void delete(Long id) {
        requestRepository.deleteById(id);
    }

    public List<RequestResponseDto> findAll() {
        List<Request> requests = requestRepository.findAll();
        return requests.stream().map(RequestResponseDto::fromEntity).toList();
    }

    public RequestResponseDto update(Long id, CreateRequestDto createRequestDto) {
        Optional<Request> existingRequest = requestRepository.findById(id);

        Request request = existingRequest.get();

        request.setTitle(createRequestDto.title());
        request.setDescription(createRequestDto.description());
        request.setPrice(createRequestDto.price());
        request.setMaxSignatureDate(createRequestDto.maxSignatureDate());

        requestRepository.save(request);

        return RequestResponseDto.fromEntity(request);
    }

    public Optional<RequestResponseDto> signature(Long id, User approver, SignatureRequestDto signatureRequestDto) {
        Optional<Request> existingRequest = requestRepository.findById(id);

        if(existingRequest.isEmpty()) {
            return Optional.empty();
        }

        Request request = existingRequest.get();
        request.setSignature(signatureRequestDto.signature());
        request.setApprover(approver);
        request.setSignatureDate(new Date());

        requestRepository.save(request);

        return Optional.of(RequestResponseDto.fromEntity(request));
    }
}
