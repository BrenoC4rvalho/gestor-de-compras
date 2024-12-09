package com.example.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Table(name = "requests")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Float price;

    @ManyToOne
    @JoinColumn(name = "approver_id", nullable = true)
    private User approver;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Setter
    @Column(name = "signature", columnDefinition = "LONGTEXT")
    private String signature;

    @Temporal(TemporalType.TIMESTAMP)
    private Date signatureDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at", nullable = false, updatable = false)
    @Builder.Default
    private Date createAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date maxSignatureDate;

}
