package com.example.post.domain.model;

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
public class Post {

    private Long id;
    private String title;
    private String content;
    private boolean deleted = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Post create(String title, String content) {
        validateTitle(title);
        validateContent(content);
        Post post = new Post();
        post.title = title;
        post.content = content;
        return post;
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

    public void update(String title, String content) {
        validateTitle(title);
        validateContent(content);
        this.title = title;
        this.updatedAt = LocalDateTime.now();
        this.content = content;
    }

    public void delete() {
        this.deleted = true;
    }

    private static void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_POST_TITLE);
        }
    }

    private static void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_POST_CONTENT);
        }
    }
}
