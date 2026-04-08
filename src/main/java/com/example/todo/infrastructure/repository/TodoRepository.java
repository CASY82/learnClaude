package com.example.todo.infrastructure.repository;

import com.example.todo.domain.model.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoRepository {

    Todo save(Todo todo);

    Optional<Todo> findByIdAndDeletedFalse(Long id);

    List<Todo> findAllByDeletedFalse();
}
