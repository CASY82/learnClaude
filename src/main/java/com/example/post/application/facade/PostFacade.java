package com.example.post.application.facade;

import com.example.post.application.dto.PostCreateRequest;
import com.example.post.application.dto.PostResponse;
import com.example.post.application.dto.PostUpdateRequest;
import com.example.post.application.service.PostService;
import com.example.post.domain.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostFacade {

    private final PostService postService;

    public PostResponse createPost(PostCreateRequest request) {
        Post post = Post.create(request.getTitle(), request.getContent());
        Post saved = postService.createPost(post);
        return PostResponse.from(saved);
    }

    public PostResponse getPost(Long id) {
        Post post = postService.findPost(id);
        return PostResponse.from(post);
    }

    public List<PostResponse> getPosts() {
        return postService.findAllPosts().stream()
                .map(PostResponse::from)
                .toList();
    }

    public PostResponse updatePost(Long id, PostUpdateRequest request) {
        Post post = postService.updatePost(id, request.getTitle(), request.getContent());
        return PostResponse.from(post);
    }

    public void deletePost(Long id) {
        postService.deletePost(id);
    }
}
