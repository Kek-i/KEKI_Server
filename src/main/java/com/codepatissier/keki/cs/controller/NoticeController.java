package com.codepatissier.keki.cs.controller;

import com.codepatissier.keki.cs.service.NoticeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "Bearer")
@Tag(name = "notices", description = "공지사항 API")
@RestController
@RequestMapping(value = "/notices")
@RequiredArgsConstructor
public class NoticeController {
    private NoticeService noticeService;

}
