package com.example.bookmark.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookmarkUpdateRequest {

    @NotBlank(message = "북마크 제목은 비어 있을 수 없습니다.")
    private String title;

    @NotBlank(message = "URL은 비어 있을 수 없습니다.")
    private String url;

    private String description;

    private String category;

    public BookmarkUpdateRequest(String title, String url, String description, String category) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.category = category;
    }
}
