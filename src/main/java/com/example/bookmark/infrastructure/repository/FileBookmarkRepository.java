package com.example.bookmark.infrastructure.repository;

import com.example.bookmark.domain.model.Bookmark;
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
public class FileBookmarkRepository implements BookmarkRepository {

    private final ObjectMapper objectMapper;
    private final String filePath;

    public FileBookmarkRepository(@Value("${bookmark.storage.file-path}") String filePath) {
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
    public synchronized Bookmark save(Bookmark bookmark) {
        List<Bookmark> bookmarks = readAll();

        if (bookmark.getId() == null) {
            long nextId = bookmarks.stream()
                    .mapToLong(Bookmark::getId)
                    .max()
                    .orElse(0L) + 1;
            bookmark.assignId(nextId);
            bookmark.assignCreatedAt(LocalDateTime.now());
            bookmark.assignUpdatedAt(LocalDateTime.now());
            bookmarks.add(bookmark);
        } else {
            bookmarks.replaceAll(b -> b.getId().equals(bookmark.getId()) ? bookmark : b);
        }

        writeAll(bookmarks);
        return bookmark;
    }

    @Override
    public Optional<Bookmark> findByIdAndDeletedFalse(Long id) {
        return readAll().stream()
                .filter(b -> b.getId().equals(id) && !b.isDeleted())
                .findFirst();
    }

    @Override
    public List<Bookmark> findAllByDeletedFalse() {
        return readAll().stream()
                .filter(b -> !b.isDeleted())
                .toList();
    }

    @Override
    public Optional<Bookmark> findByUrlAndDeletedFalse(String url) {
        return readAll().stream()
                .filter(b -> b.getUrl().equals(url) && !b.isDeleted())
                .findFirst();
    }

    private List<Bookmark> readAll() {
        try {
            File file = new File(filePath);
            if (!file.exists() || file.length() == 0) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Bookmark>>() {});
        } catch (IOException e) {
            log.error("북마크 파일 읽기 실패: {}", filePath, e);
            return new ArrayList<>();
        }
    }

    private void writeAll(List<Bookmark> bookmarks) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), bookmarks);
        } catch (IOException e) {
            log.error("북마크 파일 쓰기 실패: {}", filePath, e);
        }
    }
}
