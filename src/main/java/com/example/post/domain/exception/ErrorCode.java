package com.example.post.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    INVALID_POST_TITLE(HttpStatus.BAD_REQUEST, "제목은 비어 있을 수 없습니다."),
    INVALID_POST_CONTENT(HttpStatus.BAD_REQUEST, "내용은 비어 있을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
