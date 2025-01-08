package com.example.alamnibackend.repository;

import com.example.alamnibackend.models.QuizResult;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuizResultRepository extends MongoRepository<QuizResult, String> {

}
