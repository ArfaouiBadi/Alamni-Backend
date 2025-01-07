package com.example.alamnibackend.service;

import com.example.alamnibackend.models.CategoryCount;
import com.example.alamnibackend.models.Course;
import com.example.alamnibackend.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(String id) {
        return courseRepository.findById(id).orElse(null);
    }

    public Course createCourse(Course course) {
        System.out.println("course"+course);
        return courseRepository.save(course);
    }

    public Course updateCourse(String id, Course courseDetails) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        System.out.println("courseDetails"+courseDetails);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            course.setTitle(courseDetails.getTitle());
            course.setDescription(courseDetails.getDescription());
            course.setLevelRequired(courseDetails.getLevelRequired());
            course.setPointsRequired(courseDetails.getPointsRequired());
            course.setDuration(courseDetails.getDuration());
            course.setCategory(courseDetails.getCategory());
            course.setModules(courseDetails.getModules());
            course.setRewardSystem(courseDetails.getRewardSystem());
            course.setImageUrl(courseDetails.getImageUrl());

            return courseRepository.save(course);
        }
        return null;
    }

    public boolean deleteCourse(String id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public long getTotalCourses() {
        return courseRepository.count();
    }
    public List<CategoryCount> getCoursesPerCategory() {
        return courseRepository.countCoursesByCategory();
    }



}
