package com.example.alamnibackend.controllers;

import com.example.alamnibackend.models.CategoryCount;
import com.example.alamnibackend.models.Course;
import com.example.alamnibackend.payload.response.MessageResponse;
import com.example.alamnibackend.service.CourseService;
import com.example.alamnibackend.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")

public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable String id) {
        Course course = courseService.getCourseById(id);
        if (course != null) {
            return ResponseEntity.ok(course);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Course not found."));
        }
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public Course createCourse(@ModelAttribute Course course) throws IOException {
        System.out.println(course);
        return courseService.createCourse(course);
    }

    @PutMapping
    public ResponseEntity<?> updateCourse(@RequestBody Course courseDetails) {
        System.out.println(courseDetails);
        System.out.println(courseDetails.getId());
        Course updatedCourse = courseService.updateCourse(courseDetails.getId(), courseDetails);
        if (updatedCourse != null) {
            return ResponseEntity.ok(updatedCourse);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Course not found."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable String id) {
        boolean isDeleted = courseService.deleteCourse(id);
        if (isDeleted) {
            return ResponseEntity.ok(new MessageResponse("Course deleted successfully."));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Course not found."));
        }
    }
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalCourses() {
        long totalCourses = courseService.getTotalCourses();
        return ResponseEntity.ok(totalCourses);
    }
    @GetMapping("/courses-per-category")
    public List<CategoryCount> getCoursesPerCategory() {
        return courseService.getCoursesPerCategory();
    }


}
