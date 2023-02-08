package com.codepatissier.keki.history.repository;

import com.codepatissier.keki.history.dto.SearchRes;
import com.codepatissier.keki.history.entity.SearchHistory;
import com.codepatissier.keki.user.entity.User;

import java.util.List;

public interface SearchHistoryCustom {
    List<SearchRes> getPopularSearches();
    List<SearchHistory> getRecentSearches(User user, String status);
}
