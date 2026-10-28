package com.example.client.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Course {

    private Integer id;

    @NotBlank(message = "Tên môn học không được để trống")
    private String name;

    @NotNull(message = "Điểm không được để trống")
    @DecimalMin(value = "0", message = "Điểm phải từ 0 đến 10")
    @DecimalMax(value = "10", message = "Điểm phải từ 0 đến 10")
    private Double score;

    private Integer studentId;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }
}
