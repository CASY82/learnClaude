package com.example.bookmark.application.dto;

import com.example.bookmark.domain.model.Bookmark;

import java.time.LocalDateTime;

public record BookmarkResponse(
        Long id,
        String title,
        String url,
        String description,
        String category,
        boolean pinned,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static BookmarkResponse from(Bookmark bookmark) {
        return new BookmarkResponse(
                bookmark.getId(),
                bookmark.getTitle(),
                bookmark.getUrl(),
                bookmark.getDescription(),
                bookmark.getCategory(),
                bookmark.isPinned(),
                bookmark.getCreatedAt(),
                bookmark.getUpdatedAt()
        );
    }
}
