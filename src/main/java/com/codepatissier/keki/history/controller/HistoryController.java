package com.codepatissier.keki.history.controller;

import com.codepatissier.keki.history.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/histories")
@RequiredArgsConstructor
public class HistoryController {
    private SearchHistoryService searchHistoryService;

}
