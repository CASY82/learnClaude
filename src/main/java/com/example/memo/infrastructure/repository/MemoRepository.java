package com.example.memo.infrastructure.repository;

import com.example.memo.domain.model.Memo;

import java.util.List;
import java.util.Optional;

public interface MemoRepository {

    Memo save(Memo memo);

    Optional<Memo> findByIdAndDeletedFalse(Long id);

    List<Memo> findAllByDeletedFalse();
}
