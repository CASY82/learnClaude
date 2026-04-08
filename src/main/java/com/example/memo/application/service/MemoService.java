package com.example.memo.application.service;

import com.example.memo.domain.model.Memo;
import com.example.memo.infrastructure.repository.MemoRepository;
import com.example.post.domain.exception.BusinessException;
import com.example.post.domain.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;

    public Memo createMemo(Memo memo) {
        return memoRepository.save(memo);
    }

    public Memo findMemo(Long id) {
        return memoRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMO_NOT_FOUND));
    }

    public List<Memo> findAllMemos() {
        return memoRepository.findAllByDeletedFalse();
    }

    public Memo updateMemo(Long id, String content) {
        Memo memo = findMemo(id);
        memo.update(content);
        return memoRepository.save(memo);
    }

    public void deleteMemo(Long id) {
        Memo memo = findMemo(id);
        memo.delete();
        memoRepository.save(memo);
    }
}
