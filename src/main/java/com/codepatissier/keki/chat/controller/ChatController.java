package com.codepatissier.keki.chat.controller;

import com.codepatissier.keki.chat.dto.ChatMessage;
import com.codepatissier.keki.chat.service.ChatService;
import com.codepatissier.keki.common.BaseException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "Bearer")
@Tag(name = "chats", description = "채팅 API")
@RestController
@RequestMapping(value = "/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    //Client가 SEND할 수 있는 경로
    //stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
    //"/pub/chat/enter"
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessage message) throws BaseException {
        chatService.enter(message);
    }

    // 채팅 전송
    @MessageMapping(value = "/chats/message")
    public void message(ChatMessage message) throws BaseException{
        chatService.message(message);
    }


}
