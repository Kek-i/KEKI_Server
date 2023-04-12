package com.codepatissier.keki.chat.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessage {
    private String chatRoomId;
    private String message;
    private Long userIdx;
    private LocalDateTime timestamp;

    public void setMessage(String message) {
        this.message = message;
    }
}
