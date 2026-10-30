package com.example.client.service;

import com.example.client.model.Student;
import com.example.client.model.StudentSummary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// CRUD sinh viên kế thừa từ BaseApiService
@Service
public class StudentApiService extends BaseApiService<Student> {

    public StudentApiService(RestTemplate restTemplate) {
        super(restTemplate, "/students", Student.class, Student[].class);
    }

    public StudentSummary getSummary(Integer id) {
        return restTemplate.getForObject("/students/{id}/summary", StudentSummary.class, id);
    }
}
