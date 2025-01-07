package com.example.alamnibackend.repository;

import com.example.alamnibackend.models.CategoryCount;
import com.example.alamnibackend.models.Course;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CourseRepository extends MongoRepository<Course, String> {
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'categories', 'localField': 'category.$id', 'foreignField': '_id', 'as': 'categoryDetails' } }",
            "{ '$unwind': { 'path': '$categoryDetails', 'preserveNullAndEmptyArrays': false } }",
            "{ '$group': { '_id': '$categoryDetails.name', 'count': { '$sum': 1 } } }",
            "{ '$project': { 'category': '$_id', 'count': 1, '_id': 0 } }"
    })
    List<CategoryCount> countCoursesByCategory();



}
