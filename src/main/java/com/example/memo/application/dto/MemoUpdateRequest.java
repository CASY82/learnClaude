package com.example.memo.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemoUpdateRequest {

    @NotBlank(message = "메모 내용은 비어 있을 수 없습니다.")
    private String content;

    public MemoUpdateRequest(String content) {
        this.content = content;
    }
}
