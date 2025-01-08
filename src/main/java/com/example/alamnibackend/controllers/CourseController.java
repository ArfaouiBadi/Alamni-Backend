package com.example.alamnibackend.controllers;

import com.example.alamnibackend.models.CategoryCount;
import com.example.alamnibackend.models.Course;
import com.example.alamnibackend.models.Enrollment;
import com.example.alamnibackend.models.User;
import com.example.alamnibackend.payload.response.MessageResponse;
import com.example.alamnibackend.repository.EnrollmentRepository;
import com.example.alamnibackend.repository.UserRepository;
import com.example.alamnibackend.service.CourseService;
import com.example.alamnibackend.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")

public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

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

    @PutMapping
    public ResponseEntity<?> updateCourse(@RequestBody Course courseDetails) {
        Course updatedCourse = courseService.updateCourse(courseDetails.getId(), courseDetails);

        if (updatedCourse != null) {
            notifyEnrolledUsers(courseDetails.getId(), "Course updated", "The course you are enrolled in has been updated.");
            return ResponseEntity.ok(updatedCourse);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Course not found."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable String id) {
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(id);
        System.out.println(enrollments);
        for (Enrollment enrollment : enrollments) {
            User user = enrollment.getUser();
            user.setPoints(user.getPoints() + enrollment.getCourse().getPointsRequired());
            userRepository.save(user);
            notifyUser(user.getEmail(), "Course deleted", "The course you were enrolled in has been deleted. Your points have been refunded.");
        }
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

    private void notifyEnrolledUsers(String courseId, String subject, String message) {
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        for (Enrollment enrollment : enrollments) {
            notifyUser(enrollment.getUser().getEmail(), subject, message);
        }
    }

    private void notifyUser(String email, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }
}
