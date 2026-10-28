package com.example.api.controller;

import com.example.api.entity.Course;
import com.example.api.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseApiController {

    private final CourseService courseService;

    public CourseApiController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    public Course getById(@PathVariable Integer id) {
        return courseService.findById(id);
    }

    @PutMapping("/{id}")
    public Course update(@PathVariable Integer id, @Valid @RequestBody Course course) {
        return courseService.update(id, course);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        courseService.delete(id);
    }
}
