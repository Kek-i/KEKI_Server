package com.codepatissier.keki.cs.service;

import com.codepatissier.keki.cs.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CsService {
    private final NoticeRepository noticeRepository;
}
