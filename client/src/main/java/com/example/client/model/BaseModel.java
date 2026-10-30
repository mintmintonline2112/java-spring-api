package com.example.client.model;

// Lớp cha cho mọi model nhận từ Web API: có id
public abstract class BaseModel {

    private Integer id;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
}
