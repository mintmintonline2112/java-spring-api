package com.example.api.controller;

import com.example.api.entity.BaseEntity;
import com.example.api.service.BaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Web API CRUD dùng chung. Controller con chỉ cần khai báo @RestController + @RequestMapping
 * và truyền service vào là có đủ:
 * GET /?keyword=  |  GET /{id}  |  POST /  |  PUT /{id}  |  DELETE /{id}
 */
public abstract class BaseApiController<T extends BaseEntity> {

    protected final BaseService<T> service;

    protected BaseApiController(BaseService<T> service) {
        this.service = service;
    }

    @GetMapping
    public List<T> getAll(@RequestParam(required = false) String keyword) {
        return service.findAll(keyword);
    }

    @GetMapping("/{id}")
    public T getById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public T create(@Valid @RequestBody T entity) {
        return service.create(entity);
    }

    @PutMapping("/{id}")
    public T update(@PathVariable Integer id, @Valid @RequestBody T entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
