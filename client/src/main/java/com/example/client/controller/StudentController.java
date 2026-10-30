package com.example.client.controller;

import com.example.client.model.Student;
import com.example.client.service.CourseApiService;
import com.example.client.service.StudentApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Câu 1: liệt kê, thêm, sửa, xóa, tìm sinh viên theo tên -> kế thừa từ BaseMvcController
@Controller
@RequestMapping("/students")
public class StudentController extends BaseMvcController<Student> {

    private final StudentApiService studentApiService;
    private final CourseApiService courseApiService;

    public StudentController(StudentApiService studentApiService, CourseApiService courseApiService) {
        super(studentApiService, "student", "students", "sinh viên");
        this.studentApiService = studentApiService;
        this.courseApiService = courseApiService;
    }

    @Override
    protected Student newItem() {
        return new Student();
    }

    // Câu 2 + 3: chọn sinh viên -> danh sách môn học, điểm TB, xếp loại, tìm môn học theo tên/điểm
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id,
                         @RequestParam(required = false) String courseName,
                         @RequestParam(required = false) Double minScore,
                         @RequestParam(required = false) Double maxScore,
                         Model model) {
        model.addAttribute("student", studentApiService.findById(id));
        if (minScore != null && maxScore != null && minScore > maxScore) {
            model.addAttribute("searchError", "Điểm từ phải nhỏ hơn hoặc bằng điểm đến");
            model.addAttribute("courses", courseApiService.findByStudent(id, courseName, null, null));
        } else {
            model.addAttribute("courses", courseApiService.findByStudent(id, courseName, minScore, maxScore));
        }
        model.addAttribute("summary", studentApiService.getSummary(id));
        model.addAttribute("courseName", courseName);
        model.addAttribute("minScore", minScore);
        model.addAttribute("maxScore", maxScore);
        return "students/detail";
    }
}
