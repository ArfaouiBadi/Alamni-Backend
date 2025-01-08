package com.example.alamnibackend.repository;

import com.example.alamnibackend.models.Enrollment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {
    @Query("{ 'user.$id': ObjectId(?0) }")
    List<Enrollment> findByUserId(String userId);
    long countByCourseId(String courseId);
    List<Enrollment> findByCourseId(String courseId);
}