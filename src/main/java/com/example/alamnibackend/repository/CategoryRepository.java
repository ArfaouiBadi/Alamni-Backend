package com.example.alamnibackend.repository;

import com.example.alamnibackend.models.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
}