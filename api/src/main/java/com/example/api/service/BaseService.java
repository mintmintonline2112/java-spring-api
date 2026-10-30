package com.example.api.service;

import com.example.api.entity.BaseEntity;
import com.example.api.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Service CRUD dùng chung. Entity mới chỉ cần kế thừa và cài đặt copyData()
 * (các trường được phép sửa), có thể override search() để tìm kiếm theo từ khóa.
 */
public abstract class BaseService<T extends BaseEntity> {

    protected final JpaRepository<T, Integer> repository;
    private final String entityName;

    protected BaseService(JpaRepository<T, Integer> repository, String entityName) {
        this.repository = repository;
        this.entityName = entityName;
    }

    // Liệt kê tất cả, hoặc tìm theo từ khóa nếu có
    public List<T> findAll(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return repository.findAll();
        }
        return search(keyword.trim());
    }

    // Mặc định không hỗ trợ tìm kiếm: trả về tất cả
    protected List<T> search(String keyword) {
        return repository.findAll();
    }

    public T findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy " + entityName + " id = " + id));
    }

    public T create(T entity) {
        entity.setId(null);
        return repository.save(entity);
    }

    public T update(Integer id, T data) {
        T entity = findById(id);
        copyData(data, entity);
        return repository.save(entity);
    }

    public void delete(Integer id) {
        repository.delete(findById(id));
    }

    // Chép các trường được phép sửa từ source sang target
    protected abstract void copyData(T source, T target);
}
