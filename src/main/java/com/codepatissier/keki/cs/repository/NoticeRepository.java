package com.codepatissier.keki.cs.repository;

import com.codepatissier.keki.cs.entity.Notice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findAllByStatusEquals(String status, Sort sort);
    Optional<Notice> findByNoticeIdxAndStatusEquals(Long noticeIdx, String status);
}
