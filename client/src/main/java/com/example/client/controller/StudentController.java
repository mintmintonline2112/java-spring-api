package com.example.client.controller;

import com.example.client.model.Student;
import com.example.client.service.CourseApiService;
import com.example.client.service.StudentApiService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentApiService studentApiService;
    private final CourseApiService courseApiService;

    public StudentController(StudentApiService studentApiService, CourseApiService courseApiService) {
        this.studentApiService = studentApiService;
        this.courseApiService = courseApiService;
    }

    // Câu 1: liệt kê + tìm sinh viên theo tên
    @GetMapping
    public String list(@RequestParam(required = false) String name, Model model) {
        model.addAttribute("students", studentApiService.findAll(name));
        model.addAttribute("name", name);
        return "students/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("student", new Student());
        return "students/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("student", studentApiService.findById(id));
        return "students/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("student") Student student, BindingResult result,
                       RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "students/form";
        }
        boolean isNew = student.getId() == null;
        studentApiService.save(student);
        ra.addFlashAttribute("message", isNew ? "Thêm sinh viên thành công" : "Cập nhật sinh viên thành công");
        return "redirect:/students";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        studentApiService.delete(id);
        ra.addFlashAttribute("message", "Đã xóa sinh viên");
        return "redirect:/students";
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
