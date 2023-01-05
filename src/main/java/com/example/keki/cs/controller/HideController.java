package com.example.keki.cs.controller;

import com.example.keki.history.service.PostHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/hides")
@RequiredArgsConstructor
public class HideController {
    private PostHistoryService postHistoryService;

}
