package com.example.alamnibackend.service;

import com.example.alamnibackend.models.Course;
import com.example.alamnibackend.models.Enrollment;
import com.example.alamnibackend.models.User;
import com.example.alamnibackend.repository.CourseRepository;
import com.example.alamnibackend.repository.EnrollmentRepository;
import com.example.alamnibackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public Enrollment enrollInCourse(String userId, String courseId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Course> courseOptional = courseRepository.findById(courseId);

        if (userOptional.isPresent() && courseOptional.isPresent()) {
            User user = userOptional.get();
            Course course = courseOptional.get();
            if (user.getEnrolledCourses().contains(course)) {
                throw new RuntimeException("User already enrolled in course");
            }
            if (user.getLevel() < course.getLevelRequired()) {
                throw new RuntimeException("User level is not high enough to enroll in course");
            }
            if (user.getPoints() < course.getRewardSystem().getPoints()) {
                throw new RuntimeException("User points are not high enough to enroll in course");
            }
            Enrollment enrollment = new Enrollment();
            enrollment.setUser(user);
            enrollment.setCourse(course);
            enrollment.setProgress(0);
            enrollment.setFinished(false);
            enrollment.setStartDate(new Date());
            enrollment.setLastVisitedDate(new Date());
            return enrollmentRepository.save(enrollment);
        } else {
            throw new RuntimeException("User or Course not found");
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
}