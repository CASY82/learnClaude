package com.example.post.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    INVALID_POST_TITLE(HttpStatus.BAD_REQUEST, "제목은 비어 있을 수 없습니다."),
    INVALID_POST_CONTENT(HttpStatus.BAD_REQUEST, "내용은 비어 있을 수 없습니다."),

    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "할일을 찾을 수 없습니다."),
    INVALID_TODO_TITLE(HttpStatus.BAD_REQUEST, "할일 제목은 비어 있을 수 없습니다."),

    MEMO_NOT_FOUND(HttpStatus.NOT_FOUND, "메모를 찾을 수 없습니다."),
    INVALID_MEMO_CONTENT(HttpStatus.BAD_REQUEST, "메모 내용은 비어 있을 수 없습니다."),

    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "북마크를 찾을 수 없습니다."),
    INVALID_BOOKMARK_TITLE(HttpStatus.BAD_REQUEST, "북마크 제목은 비어 있을 수 없습니다."),
    INVALID_BOOKMARK_URL(HttpStatus.BAD_REQUEST, "URL은 비어 있을 수 없습니다."),
    INVALID_BOOKMARK_URL_FORMAT(HttpStatus.BAD_REQUEST, "URL은 http:// 또는 https://로 시작해야 합니다."),
    DUPLICATE_BOOKMARK_URL(HttpStatus.CONFLICT, "이미 등록된 URL입니다.");

    private final HttpStatus status;
    private final String message;
}
