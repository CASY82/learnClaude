package com.example.bookmark.infrastructure.repository;

import com.example.bookmark.domain.model.Bookmark;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository {

    Bookmark save(Bookmark bookmark);

    Optional<Bookmark> findByIdAndDeletedFalse(Long id);

    List<Bookmark> findAllByDeletedFalse();

    Optional<Bookmark> findByUrlAndDeletedFalse(String url);
}
