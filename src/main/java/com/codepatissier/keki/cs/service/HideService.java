package com.codepatissier.keki.cs.service;

import com.codepatissier.keki.cs.repository.HideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HideService {
    private final HideRepository hideRepository;
}