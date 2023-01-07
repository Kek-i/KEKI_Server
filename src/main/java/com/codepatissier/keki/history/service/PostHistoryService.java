package com.codepatissier.keki.history.service;

import com.codepatissier.keki.history.repository.PostHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostHistoryService {
    private final PostHistoryRepository postHistoryRepository;
}
