package com.example.keki.history.service;

import com.example.keki.history.repository.SearchHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {
    private final SearchHistoryRepository searchHistoryRepository;
}
