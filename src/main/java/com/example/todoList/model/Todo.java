package com.example.todoList.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "todo_list")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "_id")
    @JsonProperty("_id")
    private long id;

    @Column(name = "value")
    private String value;

    @Column(name = "is_completed")
    @JsonProperty("isCompleted")
    private boolean isCompleted;

    public Todo() {
    }

    public Todo(String value, boolean isCompleted) {
        this.value = value;
        this.isCompleted = isCompleted;
    }

    public long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    @JsonProperty("isCompleted")
    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
} 