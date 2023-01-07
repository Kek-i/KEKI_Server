package com.codepatissier.keki.cs.controller;

import com.codepatissier.keki.history.service.PostHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/hides")
@RequiredArgsConstructor
public class HideController {
    private PostHistoryService postHistoryService;

}
