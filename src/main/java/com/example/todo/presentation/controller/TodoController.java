package com.example.todo.presentation.controller;

import com.example.todo.application.dto.TodoCreateRequest;
import com.example.todo.application.dto.TodoResponse;
import com.example.todo.application.dto.TodoUpdateRequest;
import com.example.todo.application.facade.TodoFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoFacade todoFacade;

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@Valid @RequestBody TodoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoFacade.createTodo(request));
    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> getTodos() {
        return ResponseEntity.ok(todoFacade.getTodos());
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TodoResponse> toggleTodo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(todoFacade.toggleTodo(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateTodo(
            @PathVariable("id") Long id,
            @Valid @RequestBody TodoUpdateRequest request
    ) {
        return ResponseEntity.ok(todoFacade.updateTodo(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable("id") Long id) {
        todoFacade.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
}
