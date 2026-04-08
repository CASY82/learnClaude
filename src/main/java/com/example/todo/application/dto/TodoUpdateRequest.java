package com.example.todo.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TodoUpdateRequest {

    @NotBlank(message = "할일 제목은 비어 있을 수 없습니다.")
    private String title;

    public TodoUpdateRequest(String title) {
        this.title = title;
    }
}
