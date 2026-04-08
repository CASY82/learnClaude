package com.example.memo.infrastructure.repository;

import com.example.memo.domain.model.Memo;
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
public class FileMemoRepository implements MemoRepository {

    private final ObjectMapper objectMapper;
    private final String filePath;

    public FileMemoRepository(@Value("${memo.storage.file-path}") String filePath) {
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
    public synchronized Memo save(Memo memo) {
        List<Memo> memos = readAll();

        if (memo.getId() == null) {
            long nextId = memos.stream()
                    .mapToLong(Memo::getId)
                    .max()
                    .orElse(0L) + 1;
            memo.assignId(nextId);
            memo.assignCreatedAt(LocalDateTime.now());
            memo.assignUpdatedAt(LocalDateTime.now());
            memos.add(memo);
        } else {
            memos.replaceAll(m -> m.getId().equals(memo.getId()) ? memo : m);
        }

        writeAll(memos);
        return memo;
    }

    @Override
    public Optional<Memo> findByIdAndDeletedFalse(Long id) {
        return readAll().stream()
                .filter(m -> m.getId().equals(id) && !m.isDeleted())
                .findFirst();
    }

    @Override
    public List<Memo> findAllByDeletedFalse() {
        return readAll().stream()
                .filter(m -> !m.isDeleted())
                .toList();
    }

    private List<Memo> readAll() {
        try {
            File file = new File(filePath);
            if (!file.exists() || file.length() == 0) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Memo>>() {});
        } catch (IOException e) {
            log.error("메모 파일 읽기 실패: {}", filePath, e);
            return new ArrayList<>();
        }
    }

    private void writeAll(List<Memo> memos) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), memos);
        } catch (IOException e) {
            log.error("메모 파일 쓰기 실패: {}", filePath, e);
        }
    }
}
