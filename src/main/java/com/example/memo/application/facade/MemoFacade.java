package com.example.memo.application.facade;

import com.example.memo.application.dto.MemoCreateRequest;
import com.example.memo.application.dto.MemoResponse;
import com.example.memo.application.dto.MemoUpdateRequest;
import com.example.memo.application.service.MemoService;
import com.example.memo.domain.model.Memo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemoFacade {

    private final MemoService memoService;

    public MemoResponse createMemo(MemoCreateRequest request) {
        Memo memo = Memo.create(request.getContent());
        Memo saved = memoService.createMemo(memo);
        return MemoResponse.from(saved);
    }

    public MemoResponse getMemo(Long id) {
        Memo memo = memoService.findMemo(id);
        return MemoResponse.from(memo);
    }

    public List<MemoResponse> getMemos() {
        return memoService.findAllMemos().stream()
                .map(MemoResponse::from)
                .toList();
    }

    public MemoResponse updateMemo(Long id, MemoUpdateRequest request) {
        Memo memo = memoService.updateMemo(id, request.getContent());
        return MemoResponse.from(memo);
    }

    public void deleteMemo(Long id) {
        memoService.deleteMemo(id);
    }
}
