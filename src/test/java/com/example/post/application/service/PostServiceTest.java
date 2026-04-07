package com.example.post.application.service;

import com.example.post.domain.exception.BusinessException;
import com.example.post.domain.exception.ErrorCode;
import com.example.post.domain.model.Post;
import com.example.post.infrastructure.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Test
    @DisplayName("존재하지 않는 게시글 조회 시 BusinessException 발생")
    void findPost_notFound() {
        // given
        given(postRepository.findByIdAndDeletedFalse(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> postService.findPost(1L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.POST_NOT_FOUND);
    }

    @Test
    @DisplayName("삭제된 게시글은 목록에서 제외된다")
    void findAllPosts_excludesDeleted() {
        // given
        Post post = Post.create("제목", "내용");
        given(postRepository.findAllByDeletedFalse()).willReturn(List.of(post));

        // when
        List<Post> result = postService.findAllPosts();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).isDeleted()).isFalse();
    }
}
