package com.example.api.service;

import com.example.api.entity.Student;
import com.example.api.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// CRUD sinh viên kế thừa từ BaseService
@Service
public class StudentService extends BaseService<Student> {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        super(studentRepository, "sinh viên");
        this.studentRepository = studentRepository;
    }

    // Tìm sinh viên theo tên (chứa chuỗi, không phân biệt hoa thường)
    @Override
    protected List<Student> search(String keyword) {
        return studentRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    protected void copyData(Student source, Student target) {
        target.setName(source.getName());
        target.setAddress(source.getAddress());
        target.setPhone(source.getPhone());
        target.setDob(source.getDob());
    }
}
