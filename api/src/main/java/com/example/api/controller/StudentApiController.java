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
// CRUD sinh viên kế thừa từ BaseApiController: GET/POST/PUT/DELETE, tìm theo tên qua ?keyword=
public class StudentApiController extends BaseApiController<Student> {

    private final CourseService courseService;

    public StudentApiController(StudentService studentService, CourseService courseService) {
        super(studentService);
        this.courseService = courseService;
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

    // GET/PUT/DELETE /api/students/{id}/courses/{courseId}   (chỉ thao tác môn học thuộc đúng sinh viên)
    @GetMapping("/{id}/courses/{courseId}")
    public Course getCourse(@PathVariable Integer id, @PathVariable Integer courseId) {
        return courseService.findByIdForStudent(id, courseId);
    }

    @PutMapping("/{id}/courses/{courseId}")
    public Course updateCourse(@PathVariable Integer id, @PathVariable Integer courseId,
                               @Valid @RequestBody Course course) {
        return courseService.updateForStudent(id, courseId, course);
    }

    @DeleteMapping("/{id}/courses/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable Integer id, @PathVariable Integer courseId) {
        courseService.deleteForStudent(id, courseId);
    }

    // GET /api/students/{id}/summary   (điểm trung bình + xếp loại)
    @GetMapping("/{id}/summary")
    public Map<String, Object> summary(@PathVariable Integer id) {
        return courseService.summary(id);
    }
}
