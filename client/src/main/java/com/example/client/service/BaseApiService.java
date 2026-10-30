package com.example.client.service;

import com.example.client.model.BaseModel;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Gọi Web API CRUD dùng chung (khớp với BaseApiController bên project api).
 * Service con chỉ cần truyền đường dẫn resource (vd: "/students") và kiểu dữ liệu.
 */
public abstract class BaseApiService<T extends BaseModel> {

    protected final RestTemplate restTemplate;
    private final String resourcePath;
    private final Class<T> type;
    private final Class<T[]> arrayType;

    protected BaseApiService(RestTemplate restTemplate, String resourcePath, Class<T> type, Class<T[]> arrayType) {
        this.restTemplate = restTemplate;
        this.resourcePath = resourcePath;
        this.type = type;
        this.arrayType = arrayType;
    }

    public List<T> findAll(String keyword) {
        T[] items = restTemplate.getForObject(resourcePath + "?keyword={keyword}", arrayType,
                keyword == null ? "" : keyword);
        return items == null ? List.of() : Arrays.asList(items);
    }

    public T findById(Integer id) {
        return restTemplate.getForObject(resourcePath + "/{id}", type, id);
    }

    public void save(T item) {
        if (item.getId() == null) {
            restTemplate.postForObject(resourcePath, item, type);
        } else {
            restTemplate.put(resourcePath + "/{id}", item, item.getId());
        }
    }

    public void delete(Integer id) {
        restTemplate.delete(resourcePath + "/{id}", id);
    }
}
