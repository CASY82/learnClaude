package com.example.bookmark.presentation.controller;

import com.example.bookmark.application.dto.BookmarkCreateRequest;
import com.example.bookmark.application.dto.BookmarkResponse;
import com.example.bookmark.application.dto.BookmarkUpdateRequest;
import com.example.bookmark.application.facade.BookmarkFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkFacade bookmarkFacade;

    @PostMapping
    public ResponseEntity<BookmarkResponse> createBookmark(@Valid @RequestBody BookmarkCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookmarkFacade.createBookmark(request));
    }

    @GetMapping
    public ResponseEntity<List<BookmarkResponse>> getBookmarks(
            @RequestParam(value = "category", required = false) String category) {
        return ResponseEntity.ok(bookmarkFacade.getBookmarks(category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookmarkResponse> getBookmark(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bookmarkFacade.getBookmark(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookmarkResponse> updateBookmark(
            @PathVariable("id") Long id,
            @Valid @RequestBody BookmarkUpdateRequest request
    ) {
        return ResponseEntity.ok(bookmarkFacade.updateBookmark(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable("id") Long id) {
        bookmarkFacade.deleteBookmark(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/pin")
    public ResponseEntity<BookmarkResponse> togglePin(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bookmarkFacade.togglePin(id));
    }
}
