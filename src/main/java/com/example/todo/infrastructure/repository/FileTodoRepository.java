package com.example.todo.infrastructure.repository;

import com.example.todo.domain.model.Todo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class FileTodoRepository implements TodoRepository {

    private final ObjectMapper objectMapper;
    private final String filePath;

    public FileTodoRepository(@Value("${todo.storage.file-path}") String filePath) {
        this.filePath = filePath;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @PostConstruct
    public void init() {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            writeAll(new ArrayList<>());
        }
    }

    @Override
    public synchronized Todo save(Todo todo) {
        List<Todo> todos = readAll();

        if (todo.getId() == null) {
            long nextId = todos.stream()
                    .mapToLong(Todo::getId)
                    .max()
                    .orElse(0L) + 1;
            todo.assignId(nextId);
            todo.assignCreatedAt(LocalDateTime.now());
            todo.assignUpdatedAt(LocalDateTime.now());
            todos.add(todo);
        } else {
            todos.replaceAll(t -> t.getId().equals(todo.getId()) ? todo : t);
        }

        writeAll(todos);
        return todo;
    }

    @Override
    public Optional<Todo> findByIdAndDeletedFalse(Long id) {
        return readAll().stream()
                .filter(t -> t.getId().equals(id) && !t.isDeleted())
                .findFirst();
    }

    @Override
    public List<Todo> findAllByDeletedFalse() {
        return readAll().stream()
                .filter(t -> !t.isDeleted())
                .toList();
    }

    private List<Todo> readAll() {
        try {
            File file = new File(filePath);
            if (!file.exists() || file.length() == 0) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Todo>>() {});
        } catch (IOException e) {
            log.error("할일 파일 읽기 실패: {}", filePath, e);
            return new ArrayList<>();
        }
    }

    private void writeAll(List<Todo> todos) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), todos);
        } catch (IOException e) {
            log.error("할일 파일 쓰기 실패: {}", filePath, e);
        }
    }
}
