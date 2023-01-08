package com.codepatissier.keki.history.controller;

import com.codepatissier.keki.history.service.SearchHistoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "histories", description = "검색,조회 기록 API")
@RestController
@RequestMapping(value = "/histories")
@RequiredArgsConstructor
public class HistoryController {
    private SearchHistoryService searchHistoryService;

}
