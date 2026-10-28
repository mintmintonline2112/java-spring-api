package com.example.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên môn học không được để trống")
    private String name;

    @NotNull(message = "Điểm không được để trống")
    @DecimalMin(value = "0", message = "Điểm phải từ 0 đến 10")
    @DecimalMax(value = "10", message = "Điểm phải từ 0 đến 10")
    private Double score;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // Trả về student_id trong JSON thay vì cả object student
    public Integer getStudentId() {
        return student != null ? student.getId() : null;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
}
