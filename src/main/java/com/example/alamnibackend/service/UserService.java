package com.example.alamnibackend.service;

import com.example.alamnibackend.models.Course;
import com.example.alamnibackend.models.Enrollment;
import com.example.alamnibackend.models.User;
import com.example.alamnibackend.repository.CourseRepository;
import com.example.alamnibackend.repository.EnrollmentRepository;
import com.example.alamnibackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public User registerUser(String email, String name) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setUsername(name);
            user.setEnabled(true);
            userRepository.save(user);
        }
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public User updateUser(String id, User userDetails) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setDateOfBirth(userDetails.getDateOfBirth());
            user.setRoles(userDetails.getRoles());
            user.setEnabled(userDetails.isEnabled());
            return userRepository.save(user);
        } else {
            return null;
        }
    }

    public boolean deleteUser(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    public User getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    public long getTotalUsers() {
        return userRepository.count();
    }
    public ResponseEntity<?> enrollInCourse(String userId, String courseId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (userOptional.isPresent() && courseOptional.isPresent()) {
            User user = userOptional.get();
            Course course = courseOptional.get();
            if (user.getEnrolledCourses().contains(course)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user already enrolled in course");
            }
            if (user.getLevel() < course.getLevelRequired()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user level is not high enough to enroll in course");
            }
            if (user.getPoints() < course.getRewardSystem().getPoints()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user points are not high enough to enroll in course");
            }
            Enrollment enrollment = new Enrollment();
            enrollment.setUser(user);
            enrollment.setCourse(course);
            enrollment.setProgress(0);
            enrollment.setFinished(false);
            enrollment.setStartDate(new Date());
            enrollment.setLastVisitedDate(new Date());
            enrollmentRepository.save(enrollment);
            return ResponseEntity.ok(enrollment);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user or Course not found");
        }
    }

    public boolean isUserEnrolledInCourse(String userId, String courseId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Course> courseOptional = courseRepository.findById(courseId);

        if (userOptional.isPresent() && courseOptional.isPresent()) {
            User user = userOptional.get();
            Course course = courseOptional.get();
            return user.getEnrolledCourses().contains(course);
        } else {
            throw new RuntimeException("User or Course not found");
        }
    }

    public Set<Course> getEnrolledCourses(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get().getEnrolledCourses();
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public User disenrollFromCourse(String userId, String courseId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Course> courseOptional = courseRepository.findById(courseId);

        if (userOptional.isPresent() && courseOptional.isPresent()) {
            User user = userOptional.get();
            Course course = courseOptional.get();
            user.getEnrolledCourses().remove(course);
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User or Course not found");
        }
    }
    public long getTotalEnrolledCourses() {
        List<User> users = userRepository.findAll();
        long totalEnrolledCourses = 0;

        for (User user : users) {
            totalEnrolledCourses += user.getEnrolledCourses().size();
        }

        return totalEnrolledCourses;
    }
    public Map<String, Long> getUsersByAgeGroup() {
        List<User> users = userRepository.findAll();
        Map<String, Long> ageDistribution = new HashMap<>();

        for (User user : users) {
            int age = LocalDate.now().getYear() - user.getDateOfBirth().getYear();
            String ageGroup = getAgeGroup(age);
            ageDistribution.put(ageGroup, ageDistribution.getOrDefault(ageGroup, 0L) + 1);
        }

        return ageDistribution;
    }

    private String getAgeGroup(int age) {
        if (age < 18) return "Under 18";
        if (age < 25) return "18-24";
        if (age < 35) return "25-34";
        if (age < 45) return "35-44";
        if (age < 60) return "45-59";
        return "60+";
    }
    public Set<Course> getEnrolledCoursesByUsername(String username) {
        User user = getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user.getEnrolledCourses();
    }
    public User updateUserNameById(String id, String firstName, String lastName) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            return userRepository.save(user);
        } else {
            return null;
        }
    }




}