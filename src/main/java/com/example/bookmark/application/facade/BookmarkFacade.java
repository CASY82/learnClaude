package com.example.bookmark.application.facade;

import com.example.bookmark.application.dto.BookmarkCreateRequest;
import com.example.bookmark.application.dto.BookmarkResponse;
import com.example.bookmark.application.dto.BookmarkUpdateRequest;
import com.example.bookmark.application.service.BookmarkService;
import com.example.bookmark.domain.model.Bookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookmarkFacade {

    private final BookmarkService bookmarkService;

    public BookmarkResponse createBookmark(BookmarkCreateRequest request) {
        Bookmark bookmark = Bookmark.create(
                request.getTitle(),
                request.getUrl(),
                request.getDescription(),
                request.getCategory()
        );
        Bookmark saved = bookmarkService.createBookmark(bookmark);
        return BookmarkResponse.from(saved);
    }

    public BookmarkResponse getBookmark(Long id) {
        Bookmark bookmark = bookmarkService.findBookmark(id);
        return BookmarkResponse.from(bookmark);
    }

    public List<BookmarkResponse> getBookmarks(String category) {
        List<Bookmark> bookmarks;
        if (category != null && !category.isBlank()) {
            bookmarks = bookmarkService.findAllBookmarksByCategory(category);
        } else {
            bookmarks = bookmarkService.findAllBookmarks();
        }
        return bookmarks.stream()
                .map(BookmarkResponse::from)
                .toList();
    }

    public BookmarkResponse updateBookmark(Long id, BookmarkUpdateRequest request) {
        Bookmark bookmark = bookmarkService.updateBookmark(
                id,
                request.getTitle(),
                request.getUrl(),
                request.getDescription(),
                request.getCategory()
        );
        return BookmarkResponse.from(bookmark);
    }

    public void deleteBookmark(Long id) {
        bookmarkService.deleteBookmark(id);
    }

    public BookmarkResponse togglePin(Long id) {
        Bookmark bookmark = bookmarkService.togglePin(id);
        return BookmarkResponse.from(bookmark);
    }

    public List<String> getCategories() {
        return bookmarkService.findAllBookmarks().stream()
                .map(Bookmark::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
