package com.example.keki.cs.controller;

import com.example.keki.cs.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/notices")
@RequiredArgsConstructor
public class NoticeController {
    private NoticeService noticeService;

}
