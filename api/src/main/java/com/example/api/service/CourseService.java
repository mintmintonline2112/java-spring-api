package com.example.api.service;

import com.example.api.entity.Course;
import com.example.api.entity.Student;
import com.example.api.exception.BadRequestException;
import com.example.api.exception.NotFoundException;
import com.example.api.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseService extends BaseService<Course> {

    private final CourseRepository courseRepository;
    private final StudentService studentService;

    public CourseService(CourseRepository courseRepository, StudentService studentService) {
        super(courseRepository, "môn học");
        this.courseRepository = courseRepository;
        this.studentService = studentService;
    }

    public List<Course> findByStudent(Integer studentId, String name, Double minScore, Double maxScore) {
        studentService.findById(studentId);
        if (minScore != null && maxScore != null && minScore > maxScore) {
            throw new BadRequestException("Điểm từ phải nhỏ hơn hoặc bằng điểm đến");
        }
        String keyword = name == null ? "" : name.trim();
        return courseRepository.search(studentId, keyword, minScore, maxScore);
    }

    @Override
    protected void copyData(Course source, Course target) {
        target.setName(source.getName());
        target.setScore(source.getScore());
    }

    // Lấy môn học và kiểm tra môn học có thuộc sinh viên studentId không
    public Course findByIdForStudent(Integer studentId, Integer id) {
        Course course = findById(id);
        if (!course.getStudentId().equals(studentId)) {
            throw new NotFoundException("Sinh viên id = " + studentId + " không có môn học id = " + id);
        }
        return course;
    }

    public Course create(Integer studentId, Course course) {
        Student student = studentService.findById(studentId);
        course.setId(null);
        course.setStudent(student);
        return courseRepository.save(course);
    }

    public Course updateForStudent(Integer studentId, Integer id, Course data) {
        findByIdForStudent(studentId, id);
        return update(id, data);
    }

    public void deleteForStudent(Integer studentId, Integer id) {
        courseRepository.delete(findByIdForStudent(studentId, id));
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
