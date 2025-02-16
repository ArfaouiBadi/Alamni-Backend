package com.example.alamnibackend.controllers;

import com.example.alamnibackend.models.Course;
import com.example.alamnibackend.models.Enrollment;
import com.example.alamnibackend.payload.response.MessageResponse;
import com.example.alamnibackend.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;
    @GetMapping("/user/{userId}/enrollments")
    public List<Enrollment> getEnrollmentsByUserId(@PathVariable String userId) {
        return enrollmentService.getEnrollementsByUserId(userId);
    }
    @GetMapping("/user/{userId}/courses")
    public List<Course> getCoursesByUserId(@PathVariable String userId) {
        return enrollmentService.findCoursesByUserId(userId);
    }
    @GetMapping("/user/{userId}/last-unfinished-course")
    public Optional<Course> getLastUnfinishedCourse(@PathVariable String userId) {
        return enrollmentService.findLastUnfinishedCourseByUserId(userId);
    }
    @GetMapping("/total-enrollment-count")
    public long getTotalEnrollmentCount() {
        return enrollmentService.countTotalEnrollments();
    }
    @GetMapping("/user/{userId}/unfinished-courses")
    public List<Map<String, String>> getUnfinishedCoursesByUserId(@PathVariable String userId) {
        return enrollmentService.findUnfinishedCoursesByUserId(userId);
    }
    @GetMapping("/user/{userId}/finished-courses")
    public List<Map<String, String>> getFinishedCoursesByUserId(@PathVariable String userId) {
        return enrollmentService.findFinishedCoursesByUserId(userId);
    }
    @PostMapping("/{enrollmentId}/complete-lesson/{lessonId}")
    public ResponseEntity<?> markLessonAsCompleted(@PathVariable String enrollmentId, @PathVariable String lessonId) {
        try {
            Enrollment enrollment = enrollmentService.markLessonAsCompleted(enrollmentId, lessonId);
            return ResponseEntity.ok(enrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/{courseId}/enrollments-count")
    public ResponseEntity<Long> getEnrollmentsCountByCourseId(@PathVariable String courseId) {
        long count = enrollmentService.countEnrollmentsByCourseId(courseId);
        return ResponseEntity.ok(count);
    }
    @PutMapping("/{enrollmentId}")
    public ResponseEntity<?> updateEnrollment(@PathVariable String enrollmentId, @RequestBody Enrollment enrollmentDetails) {
        Enrollment updatedEnrollment = enrollmentService.updateEnrollment(enrollmentId, enrollmentDetails);
        if (updatedEnrollment != null) {
            return ResponseEntity.ok(updatedEnrollment);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Enrollment not found."));
        }
    }

}