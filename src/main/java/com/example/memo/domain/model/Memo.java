package com.example.memo.domain.model;

import com.example.post.domain.exception.BusinessException;
import com.example.post.domain.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Memo {

    private Long id;
    private String content;
    private boolean deleted = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Memo create(String content) {
        validateContent(content);
        Memo memo = new Memo();
        memo.content = content;
        return memo;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public void assignCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void assignUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void update(String content) {
        validateContent(content);
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {
        this.deleted = true;
    }

    private static void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_MEMO_CONTENT);
        }
    }
}
