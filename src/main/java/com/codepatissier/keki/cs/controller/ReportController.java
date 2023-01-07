package com.codepatissier.keki.cs.controller;

import com.codepatissier.keki.cs.service.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "reports", description = "신고 API")
@RestController
@RequestMapping(value = "/reports")
@RequiredArgsConstructor
public class ReportController {
    private ReportService reportService;

}
