package com.example.post.infrastructure.repository;

import com.example.post.domain.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Optional<Post> findByIdAndDeletedFalse(Long id);

    List<Post> findAllByDeletedFalse();
}
