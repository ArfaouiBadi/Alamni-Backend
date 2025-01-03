package com.example.alamnibackend.controllers;

import com.example.alamnibackend.models.Course;
import com.example.alamnibackend.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/user/{userId}/courses")
    public List<Course> getCoursesByUserId(@PathVariable String userId) {
        return enrollmentService.findCoursesByUserId(userId);
    }
    @GetMapping("/user/{userId}/last-unfinished-course")
    public Optional<Course> getLastUnfinishedCourse(@PathVariable String userId) {
        return enrollmentService.findLastUnfinishedCourseByUserId(userId);
    }
}