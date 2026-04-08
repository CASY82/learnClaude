package com.example.bookmark.domain.model;

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
public class Bookmark {

    private static final String DEFAULT_CATEGORY = "미분류";
    private static final String HTTP_PREFIX = "http://";
    private static final String HTTPS_PREFIX = "https://";

    private Long id;
    private String title;
    private String url;
    private String description;
    private String category;
    private boolean pinned = false;
    private boolean deleted = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Bookmark create(String title, String url, String description, String category) {
        validateTitle(title);
        validateUrl(url);
        Bookmark bookmark = new Bookmark();
        bookmark.title = title;
        bookmark.url = url;
        bookmark.description = description;
        bookmark.category = (category == null || category.isBlank()) ? DEFAULT_CATEGORY : category;
        return bookmark;
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

    public void update(String title, String url, String description, String category) {
        validateTitle(title);
        validateUrl(url);
        this.title = title;
        this.url = url;
        this.description = description;
        this.category = (category == null || category.isBlank()) ? DEFAULT_CATEGORY : category;
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {
        this.deleted = true;
    }

    public void togglePin() {
        this.pinned = !this.pinned;
    }

    private static void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_BOOKMARK_TITLE);
        }
    }

    private static void validateUrl(String url) {
        if (url == null || url.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_BOOKMARK_URL);
        }
        if (!url.startsWith(HTTP_PREFIX) && !url.startsWith(HTTPS_PREFIX)) {
            throw new BusinessException(ErrorCode.INVALID_BOOKMARK_URL_FORMAT);
        }
    }
}
