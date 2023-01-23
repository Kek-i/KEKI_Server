package com.codepatissier.keki.history.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorySearchRes {
    private List<SearchRes> recentSearches;
    private List<SearchRes> popularSearches;
    private List<PostSearchRes> recentPostSearches;
}
