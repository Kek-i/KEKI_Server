package com.codepatissier.keki.cs.controller;

import com.codepatissier.keki.cs.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/reports")
@RequiredArgsConstructor
public class ReportController {
    private ReportService reportService;

}
