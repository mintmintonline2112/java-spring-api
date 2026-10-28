package com.example.client.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

// Bắt lỗi khi gọi Web API và hiển thị trang thông báo
@ControllerAdvice
public class ApiErrorHandler {

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public String notFound(Model model) {
        model.addAttribute("error", "Không tìm thấy dữ liệu yêu cầu.");
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
