package com.codepatissier.keki.chat.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "Bearer")
@Tag(name = "chats", description = "상품 API")
@RestController
@RequestMapping(value = "/chats")
@RequiredArgsConstructor
public class ChatRoomController {
}
