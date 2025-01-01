package com.example.alamnibackend.repository;

import com.example.alamnibackend.models.CategoryCount;
import com.example.alamnibackend.models.Course;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CourseRepository extends MongoRepository<Course, String> {
    @Aggregation(pipeline = {
            "{ '$group': { '_id': '$category', 'count': { '$sum': 1 } } }"
    })
    List<CategoryCount> countCoursesByCategory();

}
