package com.example.keki.history.controller;

import com.example.keki.history.service.PostHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/postHistories")
@RequiredArgsConstructor
public class PostHistoryController {
    private PostHistoryService postHistoryService;

}
