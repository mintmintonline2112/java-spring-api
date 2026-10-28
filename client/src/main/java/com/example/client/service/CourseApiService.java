package com.example.client.service;

import com.example.client.model.Course;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseApiService {

    private final RestTemplate restTemplate;

    public CourseApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Course> findByStudent(Integer studentId, String name, Double minScore, Double maxScore) {
        StringBuilder url = new StringBuilder("/students/{studentId}/courses?name={name}");
        Map<String, Object> params = new HashMap<>();
        params.put("studentId", studentId);
        params.put("name", name == null ? "" : name);
        if (minScore != null) {
            url.append("&minScore={minScore}");
            params.put("minScore", minScore);
        }
        if (maxScore != null) {
            url.append("&maxScore={maxScore}");
            params.put("maxScore", maxScore);
        }
        Course[] courses = restTemplate.getForObject(url.toString(), Course[].class, params);
        return courses == null ? List.of() : Arrays.asList(courses);
    }

    public Course findById(Integer id) {
        return restTemplate.getForObject("/courses/{id}", Course.class, id);
    }

    public void save(Integer studentId, Course course) {
        if (course.getId() == null) {
            restTemplate.postForObject("/students/{studentId}/courses", course, Course.class, studentId);
        } else {
            restTemplate.put("/courses/{id}", course, course.getId());
        }
    }

    public void delete(Integer id) {
        restTemplate.delete("/courses/{id}", id);
    }
}
