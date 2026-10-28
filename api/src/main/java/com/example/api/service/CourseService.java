package com.example.api.service;

import com.example.api.entity.Course;
import com.example.api.entity.Student;
import com.example.api.exception.NotFoundException;
import com.example.api.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentService studentService;

    public CourseService(CourseRepository courseRepository, StudentService studentService) {
        this.courseRepository = courseRepository;
        this.studentService = studentService;
    }

    public List<Course> findByStudent(Integer studentId, String name, Double minScore, Double maxScore) {
        studentService.findById(studentId);
        String keyword = name == null ? "" : name.trim();
        return courseRepository.search(studentId, keyword, minScore, maxScore);
    }

    public Course findById(Integer id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy môn học id = " + id));
    }

    public Course create(Integer studentId, Course course) {
        Student student = studentService.findById(studentId);
        course.setId(null);
        course.setStudent(student);
        return courseRepository.save(course);
    }

    public Course update(Integer id, Course data) {
        Course course = findById(id);
        course.setName(data.getName());
        course.setScore(data.getScore());
        return courseRepository.save(course);
    }

    public void delete(Integer id) {
        courseRepository.delete(findById(id));
    }

    /**
     * Điểm trung bình = tổng điểm các môn / số môn học, kèm xếp loại.
     */
    public Map<String, Object> summary(Integer studentId) {
        studentService.findById(studentId);
        List<Course> courses = courseRepository.findByStudent_Id(studentId);

        double total = 0;
        for (Course c : courses) {
            total += c.getScore();
        }
        double average = courses.isEmpty() ? 0 : total / courses.size();
        average = Math.round(average * 100.0) / 100.0;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("studentId", studentId);
        result.put("courseCount", courses.size());
        result.put("average", average);
        result.put("rank", courses.isEmpty() ? "Chưa có môn học" : rank(average));
        return result;
    }

    private String rank(double average) {
        if (average >= 9) return "Xuất sắc";
        if (average >= 8) return "Giỏi";
        if (average >= 6.5) return "Khá";
        if (average >= 5) return "Trung bình";
        return "Yếu";
    }
}
