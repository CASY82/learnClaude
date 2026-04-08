package com.example.todo.application.service;

import com.example.post.domain.exception.BusinessException;
import com.example.post.domain.exception.ErrorCode;
import com.example.todo.domain.model.Todo;
import com.example.todo.infrastructure.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo findTodo(Long id) {
        return todoRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.TODO_NOT_FOUND));
    }

    public List<Todo> findAllTodos() {
        return todoRepository.findAllByDeletedFalse();
    }

    public Todo updateTodo(Long id, String title) {
        Todo todo = findTodo(id);
        todo.update(title);
        return todoRepository.save(todo);
    }

    public Todo toggleTodo(Long id) {
        Todo todo = findTodo(id);
        todo.toggle();
        return todoRepository.save(todo);
    }

    public void deleteTodo(Long id) {
        Todo todo = findTodo(id);
        todo.delete();
        todoRepository.save(todo);
    }
}
