package com.codepatissier.keki.cs.controller;

import com.codepatissier.keki.history.service.PostHistoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "hides", description = "피드 숨김 API")
@RestController
@RequestMapping(value = "/hides")
@RequiredArgsConstructor
public class HideController {
    private PostHistoryService postHistoryService;

}
