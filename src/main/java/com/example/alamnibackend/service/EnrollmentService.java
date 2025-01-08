package com.example.alamnibackend.service;

import com.example.alamnibackend.models.Course;
import com.example.alamnibackend.models.Enrollment;
import com.example.alamnibackend.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;


    public List<Course> findCoursesByUserId(String userId) {
        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);
        return enrollments.stream()
                .map(Enrollment::getCourse)
                .collect(Collectors.toList());
    }
    public Enrollment markLessonAsCompleted(String enrollmentId, String lessonId) {
        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findById(enrollmentId);
        if (enrollmentOpt.isPresent()) {
            Enrollment enrollment = enrollmentOpt.get();
            if (!enrollment.getCompletedLessons().contains(lessonId)) {
                enrollment.getCompletedLessons().add(lessonId);
                enrollmentRepository.save(enrollment);
            }
            return enrollment;
        } else {
            throw new RuntimeException("Enrollment not found");
        }
    }
    public Optional<Course> findLastUnfinishedCourseByUserId(String userId) {
        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);


        return enrollments.stream()
                .filter(enrollment -> !enrollment.isFinished())
                .sorted((e1, e2) -> e2.getStartDate().compareTo(e1.getStartDate())) // Sort by startDate descending
                .map(Enrollment::getCourse)
                .findFirst();
    }
    public long countTotalEnrollments() {
        return enrollmentRepository.count();
    }
    public List<Map<String, String>> findUnfinishedCoursesByUserId(String userId) {
        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);

        return enrollments.stream()
                .filter(enrollment -> !enrollment.isFinished())
                .map(enrollment -> Map.of(
                        "title", enrollment.getCourse().getTitle(),
                        "description", enrollment.getCourse().getDescription(),
                        "imageUrl", enrollment.getCourse().getImageUrl()
                ))
                .collect(Collectors.toList());
    }
    public List<Map<String, String>> findFinishedCoursesByUserId(String userId) {
        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);

        return enrollments.stream()
                .filter(Enrollment::isFinished)
                .map(enrollment -> Map.of(
                        "title", enrollment.getCourse().getTitle(),
                        "description", enrollment.getCourse().getDescription(),
                        "imageUrl", enrollment.getCourse().getImageUrl()
                ))
                .collect(Collectors.toList());
    }
    public List<Enrollment> getEnrollementsByUserId(String userId) {
        return enrollmentRepository.findByUserId(userId);
    }
    public long countEnrollmentsByCourseId(String courseId) {
        return enrollmentRepository.countByCourseId(courseId);
    }


    public Enrollment updateEnrollment(String enrollmentId, Enrollment enrollmentDetails) {
        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findById(enrollmentId);
        if (enrollmentOpt.isPresent()) {
            Enrollment enrollment = enrollmentOpt.get();
            enrollment.setStartDate(enrollmentDetails.getStartDate());
            enrollment.setLastVisitedDate(enrollmentDetails.getLastVisitedDate());
            enrollment.setLessonsCount(enrollmentDetails.getLessonsCount());
            enrollment.setCompletedLessons(enrollmentDetails.getCompletedLessons());
            enrollment.setFinished(enrollmentDetails.isFinished());
            enrollment.setProgress(enrollmentDetails.getProgress());
            enrollmentRepository.save(enrollment);
            return enrollment;
        } else {
            return null;
        }
    }

}