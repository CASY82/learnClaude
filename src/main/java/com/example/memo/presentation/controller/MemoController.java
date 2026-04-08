package com.example.memo.presentation.controller;

import com.example.memo.application.dto.MemoCreateRequest;
import com.example.memo.application.dto.MemoResponse;
import com.example.memo.application.dto.MemoUpdateRequest;
import com.example.memo.application.facade.MemoFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memos")
@RequiredArgsConstructor
public class MemoController {

    private final MemoFacade memoFacade;

    @PostMapping
    public ResponseEntity<MemoResponse> createMemo(@Valid @RequestBody MemoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memoFacade.createMemo(request));
    }

    @GetMapping
    public ResponseEntity<List<MemoResponse>> getMemos() {
        return ResponseEntity.ok(memoFacade.getMemos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemoResponse> getMemo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(memoFacade.getMemo(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemoResponse> updateMemo(
            @PathVariable("id") Long id,
            @Valid @RequestBody MemoUpdateRequest request
    ) {
        return ResponseEntity.ok(memoFacade.updateMemo(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemo(@PathVariable("id") Long id) {
        memoFacade.deleteMemo(id);
        return ResponseEntity.noContent().build();
    }
}
