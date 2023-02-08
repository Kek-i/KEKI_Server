package com.codepatissier.keki.history.repository;

import com.codepatissier.keki.history.entity.PostHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostHistoryRepository extends JpaRepository<PostHistory, Long>, PostHistoryCustom{
    int countByPostPostIdxEquals(Long postIdx);
}
