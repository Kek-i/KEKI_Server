package com.example.keki.history.service;

import com.example.keki.history.repository.PostHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostHistoryService {
    private final PostHistoryRepository postHistoryRepository;
}
