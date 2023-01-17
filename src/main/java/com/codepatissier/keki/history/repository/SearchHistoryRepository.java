package com.codepatissier.keki.history.repository;

import com.codepatissier.keki.history.entity.SearchHistory;
import com.codepatissier.keki.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long>, SearchHistoryCustom {
    List<SearchHistory> findTop5ByUserAndStatusOrderByCreatedDateDesc(User user, String status);

    List<SearchHistory> findByUserAndStatus(User user, String activeStatus);
}
