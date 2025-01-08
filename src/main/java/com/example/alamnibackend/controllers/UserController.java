package com.example.alamnibackend.controllers;

import com.example.alamnibackend.models.*;
import com.example.alamnibackend.payload.response.MessageResponse;
import com.example.alamnibackend.repository.RoleRepository;
import com.example.alamnibackend.repository.UserRepository;
import com.example.alamnibackend.service.CourseService;
import com.example.alamnibackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CourseService courseService;
    @GetMapping
    public List<User> getAllUsers() {
        System.out.println("Get all users");
        return userRepository.findAll();

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found."));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.ok(new MessageResponse("User deleted successfully."));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found."));
        }
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUserName(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found."));
        }
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<?> updateUserRoles(@PathVariable String id, @RequestBody List<String> roleNames) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found."));
        }
        user.getRoles().clear();
        for (String roleName : roleNames) {
            Optional<Role> role = roleRepository.findByName(ERole.valueOf(roleName));
            if (role.isPresent()) {
                user.getRoles().add(role.get());
            }
        }
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User roles updated successfully."));
    }


    @PostMapping("/enroll")
    public ResponseEntity<?> enrollInCourse(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String courseId = request.get("courseId");
        if (userId == null || courseId == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("userId and courseId are required."));
        }
        System.out.println(userService.isUserEnrolledInCourse(userId, courseId));
        if (userService.isUserEnrolledInCourse(userId, courseId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("user already enrolled in course."));
        }
        ResponseEntity<?> response = userService.enrollInCourse(userId, courseId);
        if (response.getStatusCode().is2xxSuccessful()) {
            User user = userService.getUserById(userId);
            Course course = courseService.getCourseById(courseId);
            String email = user.getEmail();
            String subject = "Enrollment Confirmation";
            String message = "You have successfully enrolled in the course: " + course.getTitle() + ".\n" +
                    "You can access the course here: " + "http://localhost:4200/course-details/" + courseId;
            notifyUser(email, subject, message);
        }
        return response;
    }

    private void notifyUser(String email, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }

    @GetMapping("/enrolled-courses")
    public ResponseEntity<Set<Course>> getEnrolledCourses(@RequestParam String userId) {
        Set<Course> courses = userService.getEnrolledCourses(userId);
        return ResponseEntity.ok(courses);
    }

    @DeleteMapping("/disenroll")
    public ResponseEntity<?> disenrollFromCourse(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String courseId = request.get("courseId");
        if (userId == null || courseId == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: userId and courseId are required."));
        }
        User user = userService.disenrollFromCourse(userId, courseId);
        return ResponseEntity.ok(new MessageResponse("User disenrolled from course successfully"));
    }
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalUsers() {
        long totalUsers = userService.getTotalUsers();
        return ResponseEntity.ok(totalUsers);
    }
    @GetMapping("/total-enrolled-courses")
    public ResponseEntity<Long> getTotalEnrolledCourses() {
        long totalEnrolledCourses = userService.getTotalEnrolledCourses();
        return ResponseEntity.ok(totalEnrolledCourses);
    }
    @GetMapping("/age-distribution")
    public ResponseEntity<Map<String, Long>> getUsersByAgeGroup() {
        Map<String, Long> ageDistribution = userService.getUsersByAgeGroup();
        return ResponseEntity.ok(ageDistribution);
    }
    @GetMapping("/enrolled-courses/username/{username}")
    public ResponseEntity<?> getEnrolledCoursesByUsername(@PathVariable String username) {
        try {
            Set<Course> courses = userService.getEnrolledCoursesByUsername(username);
            return ResponseEntity.ok(courses);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
    @PutMapping("/{id}/update-name")
    public ResponseEntity<?> updateUserName(@PathVariable String id, @RequestBody Map<String, String> nameDetails) {
        String firstName = nameDetails.get("firstName");
        String lastName = nameDetails.get("lastName");

        if (firstName == null || lastName == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: firstName and lastName are required."));
        }

        User updatedUser = userService.updateUserNameById(id, firstName, lastName);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found."));
        }
    }

    @PostMapping("/{userId}/badges")
    public ResponseEntity<?> addBadgeToUser(@PathVariable String userId, @RequestBody Badge badge) {
        User updatedUser = userService.addBadgeToUser(userId, badge);
        return ResponseEntity.ok(updatedUser);
    }
    @PostMapping("/{userId}/rewards")
    public ResponseEntity<?> updateUserRewards(@PathVariable String userId, @RequestBody RewardSystem rewardData) {
        User updatedUser = userService.updateUserRewards(userId, rewardData);
        return ResponseEntity.ok(updatedUser);
    }
    @PutMapping("/{id}/update-level-points")
    public ResponseEntity<?> updateLevelAndPoints(
            @PathVariable String id,
            @RequestBody Map<String, Integer> updates) {

        Integer newLevel = updates.get("level");
        Integer newPoints = updates.get("points");

        try {
            User updatedUser = userService.updateLevelAndPoints(id, newLevel, newPoints);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }



}