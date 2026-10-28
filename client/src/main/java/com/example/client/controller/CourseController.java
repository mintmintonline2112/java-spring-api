package com.example.client.controller;

import com.example.client.model.Course;
import com.example.client.service.CourseApiService;
import com.example.client.service.StudentApiService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Câu 3: thêm, sửa, xóa môn học cho sinh viên được chọn
@Controller
@RequestMapping("/students/{studentId}/courses")
public class CourseController {

    private final CourseApiService courseApiService;
    private final StudentApiService studentApiService;

    public CourseController(CourseApiService courseApiService, StudentApiService studentApiService) {
        this.courseApiService = courseApiService;
        this.studentApiService = studentApiService;
    }

    @GetMapping("/new")
    public String createForm(@PathVariable Integer studentId, Model model) {
        model.addAttribute("student", studentApiService.findById(studentId));
        model.addAttribute("course", new Course());
        return "courses/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer studentId, @PathVariable Integer id, Model model) {
        model.addAttribute("student", studentApiService.findById(studentId));
        model.addAttribute("course", courseApiService.findById(id));
        return "courses/form";
    }

    @PostMapping("/save")
    public String save(@PathVariable Integer studentId,
                       @Valid @ModelAttribute("course") Course course, BindingResult result,
                       Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("student", studentApiService.findById(studentId));
            return "courses/form";
        }
        boolean isNew = course.getId() == null;
        courseApiService.save(studentId, course);
        ra.addFlashAttribute("message", isNew ? "Thêm môn học thành công" : "Cập nhật môn học thành công");
        return "redirect:/students/" + studentId;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer studentId, @PathVariable Integer id, RedirectAttributes ra) {
        courseApiService.delete(id);
        ra.addFlashAttribute("message", "Đã xóa môn học");
        return "redirect:/students/" + studentId;
    }
}
