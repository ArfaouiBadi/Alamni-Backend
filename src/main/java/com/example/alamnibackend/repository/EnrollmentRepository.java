package com.example.alamnibackend.repository;

import com.example.alamnibackend.models.Enrollment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
public interface EnrollmentRepository extends MongoRepository<Enrollment, Long> {
    Optional<Enrollment> findByUserIdAndCourseId(String userId, String courseId);
}