package com.example.hello.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TodoResponse {
    @JsonProperty("list")
    private List<Todo> list;

    public TodoResponse(List<Todo> list) {
        this.list = list;
    }

    public List<Todo> getList() {
        return list;
    }

    public void setList(List<Todo> list) {
        this.list = list;
    }
} 