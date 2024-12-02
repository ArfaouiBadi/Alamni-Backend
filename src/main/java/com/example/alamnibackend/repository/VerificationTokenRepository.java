package com.example.alamnibackend.repository;

import com.example.alamnibackend.models.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends MongoRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}