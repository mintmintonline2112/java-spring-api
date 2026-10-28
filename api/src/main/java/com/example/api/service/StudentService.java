package com.example.api.service;

import com.example.api.entity.Student;
import com.example.api.exception.NotFoundException;
import com.example.api.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAll(String name) {
        if (name == null || name.isBlank()) {
            return studentRepository.findAll();
        }
        return studentRepository.findByNameContainingIgnoreCase(name.trim());
    }

    public Student findById(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sinh viên id = " + id));
    }

    public Student create(Student student) {
        student.setId(null);
        return studentRepository.save(student);
    }

    public Student update(Integer id, Student data) {
        Student student = findById(id);
        student.setName(data.getName());
        student.setAddress(data.getAddress());
        student.setPhone(data.getPhone());
        student.setDob(data.getDob());
        return studentRepository.save(student);
    }

    public void delete(Integer id) {
        studentRepository.delete(findById(id));
    }
}
