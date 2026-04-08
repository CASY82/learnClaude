package com.example.bookmark.application.service;

import com.example.bookmark.domain.model.Bookmark;
import com.example.bookmark.infrastructure.repository.BookmarkRepository;
import com.example.post.domain.exception.BusinessException;
import com.example.post.domain.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public Bookmark createBookmark(Bookmark bookmark) {
        bookmarkRepository.findByUrlAndDeletedFalse(bookmark.getUrl())
                .ifPresent(existing -> {
                    throw new BusinessException(ErrorCode.DUPLICATE_BOOKMARK_URL);
                });
        return bookmarkRepository.save(bookmark);
    }

    public Bookmark findBookmark(Long id) {
        return bookmarkRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOKMARK_NOT_FOUND));
    }

    public List<Bookmark> findAllBookmarks() {
        return bookmarkRepository.findAllByDeletedFalse().stream()
                .sorted(Comparator.comparing(Bookmark::isPinned).reversed()
                        .thenComparing(Comparator.comparing(Bookmark::getCreatedAt).reversed()))
                .toList();
    }

    public List<Bookmark> findAllBookmarksByCategory(String category) {
        return bookmarkRepository.findAllByDeletedFalse().stream()
                .filter(b -> b.getCategory().equals(category))
                .sorted(Comparator.comparing(Bookmark::isPinned).reversed()
                        .thenComparing(Comparator.comparing(Bookmark::getCreatedAt).reversed()))
                .toList();
    }

    public Bookmark updateBookmark(Long id, String title, String url, String description, String category) {
        Bookmark bookmark = findBookmark(id);
        bookmarkRepository.findByUrlAndDeletedFalse(url)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessException(ErrorCode.DUPLICATE_BOOKMARK_URL);
                });
        bookmark.update(title, url, description, category);
        return bookmarkRepository.save(bookmark);
    }

    public void deleteBookmark(Long id) {
        Bookmark bookmark = findBookmark(id);
        bookmark.delete();
        bookmarkRepository.save(bookmark);
    }

    public Bookmark togglePin(Long id) {
        Bookmark bookmark = findBookmark(id);
        bookmark.togglePin();
        return bookmarkRepository.save(bookmark);
    }
}
