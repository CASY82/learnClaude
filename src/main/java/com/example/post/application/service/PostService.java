package com.example.post.application.service;

import com.example.post.domain.exception.BusinessException;
import com.example.post.domain.exception.ErrorCode;
import com.example.post.domain.model.Post;
import com.example.post.infrastructure.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post findPost(Long id) {
        return postRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }

    public List<Post> findAllPosts() {
        return postRepository.findAllByDeletedFalse();
    }

    public Post updatePost(Long id, String title, String content) {
        Post post = findPost(id);
        post.update(title, content);
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        Post post = findPost(id);
        post.delete();
        postRepository.save(post);
    }
}
