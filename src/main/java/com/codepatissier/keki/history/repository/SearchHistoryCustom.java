package com.codepatissier.keki.history.repository;

import com.codepatissier.keki.history.dto.SearchRes;

import java.util.List;

public interface SearchHistoryCustom {
    List<SearchRes> getPopularSearches();
}
