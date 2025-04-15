package com.example.todoList.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.todoList.model.Todo;
import com.example.todoList.model.TodoRequest;
import com.example.todoList.model.TodoResponse;
import com.example.todoList.repository.TodoRepository;

/**
 * Todo API Controller
 * Provides functions to create, query, update and delete todo items
 */
@RestController
@RequestMapping("/api")
public class TodoController {

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private TodoRepository todoRepository;

    @GetMapping("/todos")
    public ResponseEntity<TodoResponse> getAllTodos(@RequestParam(required = false) Boolean isCompleted) {
        try {
            List<Todo> todos;

            if (isCompleted != null) {
                todos = todoRepository.findByIsCompleted(isCompleted);
            } else {
                todos = todoRepository.findAll();
            }

            logger.debug("Retrieved todo list: {}", todos);
            logger.info("Successfully retrieved {} todo items", todos.size());
            logger.warn("Testing log output");
            System.out.println("Direct output test");
            
            return new ResponseEntity<>(new TodoResponse(todos), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to retrieve todo list", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/todo/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable("id") long id) {
        Optional<Todo> todoData = todoRepository.findById(id);

        if (todoData.isPresent()) {
            logger.info("Retrieved todo item with id: {}", id);
            return new ResponseEntity<>(todoData.get(), HttpStatus.OK);
        } else {
            logger.warn("Todo item not found with id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    test credential.helper store
    @PostMapping("/todo")
    public ResponseEntity<Todo> createTodo(@RequestBody TodoRequest request) {
        try {
            Todo todo = new Todo(request.getValue(), request.isCompleted());
            Todo savedTodo = todoRepository.save(todo);
            logger.info("Created new todo item: {}", savedTodo);
            return new ResponseEntity<>(savedTodo, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Failed to create todo item", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/todo")
    public ResponseEntity<Todo> updateTodo(@RequestBody TodoRequest request) {
        if (request.getId() == null) {
            logger.warn("Update request missing ID");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Optional<Todo> todoData = todoRepository.findById(request.getId());

        if (todoData.isPresent()) {
            Todo todo = todoData.get();
            // Toggle completion status
            todo.setIsCompleted(!todo.getIsCompleted());
            Todo updated = todoRepository.save(todo);
            logger.info("Updated todo item: {}", updated);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            logger.warn("Todo item not found for update with id: {}", request.getId());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/todo")
    public ResponseEntity<HttpStatus> deleteTodo(@RequestBody TodoRequest request) {
        if (request.getId() == null) {
            logger.warn("Delete request missing ID");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        try {
            todoRepository.deleteById(request.getId());
            logger.info("Deleted todo item with id: {}", request.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Failed to delete todo item", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/todos")
    public ResponseEntity<HttpStatus> deleteAllTodos() {
        try {
            todoRepository.deleteAll();
            logger.info("Deleted all todo items");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Failed to delete all todo items", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 