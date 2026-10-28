package com.example.api.controller;

import com.example.api.entity.Course;
import com.example.api.entity.Student;
import com.example.api.service.CourseService;
import com.example.api.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentApiController {

    private final StudentService studentService;
    private final CourseService courseService;

    public StudentApiController(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    // GET /api/students?name=...   (liệt kê + tìm theo tên)
    @GetMapping
    public List<Student> getAll(@RequestParam(required = false) String name) {
        return studentService.findAll(name);
    }

    @GetMapping("/{id}")
    public Student getById(@PathVariable Integer id) {
        return studentService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student create(@Valid @RequestBody Student student) {
        return studentService.create(student);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Integer id, @Valid @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        studentService.delete(id);
    }

    // GET /api/students/{id}/courses?name=...&minScore=...&maxScore=...
    @GetMapping("/{id}/courses")
    public List<Course> getCourses(@PathVariable Integer id,
                                   @RequestParam(required = false) String name,
                                   @RequestParam(required = false) Double minScore,
                                   @RequestParam(required = false) Double maxScore) {
        return courseService.findByStudent(id, name, minScore, maxScore);
    }

    // POST /api/students/{id}/courses   (thêm môn học cho sinh viên)
    @PostMapping("/{id}/courses")
    @ResponseStatus(HttpStatus.CREATED)
    public Course addCourse(@PathVariable Integer id, @Valid @RequestBody Course course) {
        return courseService.create(id, course);
    }

    // GET /api/students/{id}/summary   (điểm trung bình + xếp loại)
    @GetMapping("/{id}/summary")
    public Map<String, Object> summary(@PathVariable Integer id) {
        return courseService.summary(id);
    }
}
