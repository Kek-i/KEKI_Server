package com.codepatissier.keki.history.repository;

import com.codepatissier.keki.history.entity.SearchHistory;
import com.codepatissier.keki.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findTop5ByUserOrderByCreatedDateDesc(User user);
}
