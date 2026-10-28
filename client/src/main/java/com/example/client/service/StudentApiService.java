package com.example.client.service;

import com.example.client.model.Student;
import com.example.client.model.StudentSummary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class StudentApiService {

    private final RestTemplate restTemplate;

    public StudentApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Student> findAll(String name) {
        Student[] students = restTemplate.getForObject("/students?name={name}", Student[].class,
                name == null ? "" : name);
        return students == null ? List.of() : Arrays.asList(students);
    }

    public Student findById(Integer id) {
        return restTemplate.getForObject("/students/{id}", Student.class, id);
    }

    public void save(Student student) {
        if (student.getId() == null) {
            restTemplate.postForObject("/students", student, Student.class);
        } else {
            restTemplate.put("/students/{id}", student, student.getId());
        }
    }

    public void delete(Integer id) {
        restTemplate.delete("/students/{id}", id);
    }

    public StudentSummary getSummary(Integer id) {
        return restTemplate.getForObject("/students/{id}/summary", StudentSummary.class, id);
    }
}
