package com.example.alamnibackend.repository;

import com.example.alamnibackend.models.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseRepository extends MongoRepository<Course, String> {
    // You can add custom queries if needed
}
