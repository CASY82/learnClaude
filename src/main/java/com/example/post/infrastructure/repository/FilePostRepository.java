package com.example.post.infrastructure.repository;

import com.example.post.domain.model.Post;
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
public class FilePostRepository implements PostRepository {

    private final ObjectMapper objectMapper;
    private final String filePath;

    public FilePostRepository(@Value("${post.storage.file-path}") String filePath) {
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
    public synchronized Post save(Post post) {
        List<Post> posts = readAll();

        if (post.getId() == null) {
            long nextId = posts.stream()
                    .mapToLong(Post::getId)
                    .max()
                    .orElse(0L) + 1;
            post.assignId(nextId);
            post.assignCreatedAt(LocalDateTime.now());
            post.assignUpdatedAt(LocalDateTime.now());
            posts.add(post);
        } else {
            posts.replaceAll(p -> p.getId().equals(post.getId()) ? post : p);
        }

        writeAll(posts);
        return post;
    }

    @Override
    public Optional<Post> findByIdAndDeletedFalse(Long id) {
        return readAll().stream()
                .filter(p -> p.getId().equals(id) && !p.isDeleted())
                .findFirst();
    }

    @Override
    public List<Post> findAllByDeletedFalse() {
        return readAll().stream()
                .filter(p -> !p.isDeleted())
                .toList();
    }

    private List<Post> readAll() {
        try {
            File file = new File(filePath);
            if (!file.exists() || file.length() == 0) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Post>>() {});
        } catch (IOException e) {
            log.error("게시글 파일 읽기 실패: {}", filePath, e);
            return new ArrayList<>();
        }
    }

    private void writeAll(List<Post> posts) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), posts);
        } catch (IOException e) {
            log.error("게시글 파일 쓰기 실패: {}", filePath, e);
        }
    }
}
