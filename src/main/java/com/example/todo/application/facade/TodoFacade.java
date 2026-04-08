package com.example.todo.application.facade;

import com.example.todo.application.dto.TodoCreateRequest;
import com.example.todo.application.dto.TodoResponse;
import com.example.todo.application.dto.TodoUpdateRequest;
import com.example.todo.application.service.TodoService;
import com.example.todo.domain.model.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TodoFacade {

    private final TodoService todoService;

    public TodoResponse createTodo(TodoCreateRequest request) {
        Todo todo = Todo.create(request.getTitle());
        Todo saved = todoService.createTodo(todo);
        return TodoResponse.from(saved);
    }

    public TodoResponse getTodo(Long id) {
        Todo todo = todoService.findTodo(id);
        return TodoResponse.from(todo);
    }

    public List<TodoResponse> getTodos() {
        return todoService.findAllTodos().stream()
                .map(TodoResponse::from)
                .toList();
    }

    public TodoResponse updateTodo(Long id, TodoUpdateRequest request) {
        Todo todo = todoService.updateTodo(id, request.getTitle());
        return TodoResponse.from(todo);
    }

    public TodoResponse toggleTodo(Long id) {
        Todo todo = todoService.toggleTodo(id);
        return TodoResponse.from(todo);
    }

    public void deleteTodo(Long id) {
        todoService.deleteTodo(id);
    }
}
