package com.example.post.presentation.controller;

import com.example.post.application.dto.PostCreateRequest;
import com.example.post.application.dto.PostResponse;
import com.example.post.application.dto.PostUpdateRequest;
import com.example.post.application.facade.PostFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostFacade postFacade;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postFacade.createPost(request));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getPosts() {
        return ResponseEntity.ok(postFacade.getPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable("id") Long id) {
        return ResponseEntity.ok(postFacade.getPost(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable("id") Long id,
            @Valid @RequestBody PostUpdateRequest request
    ) {
        return ResponseEntity.ok(postFacade.updatePost(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id) {
        postFacade.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
