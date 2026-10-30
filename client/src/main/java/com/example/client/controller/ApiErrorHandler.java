package com.example.client.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

// Bắt lỗi khi gọi Web API và hiển thị trang thông báo
@ControllerAdvice
public class ApiErrorHandler {

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public String notFound(Model model) {
        model.addAttribute("error", "Không tìm thấy dữ liệu yêu cầu.");
        return "error";
    }

    // API trả 400: hiển thị các thông báo lỗi API gửi về (dạng {"field": "message"})
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public String badRequest(HttpClientErrorException.BadRequest ex, Model model) {
        String message;
        try {
            Map<?, ?> errors = ex.getResponseBodyAs(Map.class);
            message = errors == null || errors.isEmpty() ? null : String.join("; ",
                    errors.values().stream().map(String::valueOf).toList());
        } catch (RuntimeException e) {
            message = null;
        }
        model.addAttribute("error", "Dữ liệu không hợp lệ: " + (message != null ? message : ex.getStatusText()));
        return "error";
    }

    // Tham số trên URL sai kiểu (vd: /students/abc, minScore=abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String typeMismatch(MethodArgumentTypeMismatchException ex, Model model) {
        model.addAttribute("error", "Tham số '" + ex.getName() + "' không hợp lệ: " + ex.getValue());
        return "error";
    }

    @ExceptionHandler(ResourceAccessException.class)
    public String apiDown(Model model) {
        model.addAttribute("error", "Không kết nối được tới Web API. Hãy chạy project api (port 8080) trước.");
        return "error";
    }

    @ExceptionHandler(RestClientException.class)
    public String apiError(RestClientException ex, Model model) {
        model.addAttribute("error", "Lỗi khi gọi Web API: " + ex.getMessage());
        return "error";
    }
}
