package com.codepatissier.keki.chat.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@DynamicInsert
public class Chat {
    private String chatId;
    private String message;
    private LocalDateTime sendTime;
    private Long userIdx;

    public static Chat create(String message, Long userIdx){
        Chat chat = new Chat();
        chat.chatId = UUID.randomUUID().toString();
        chat.message = message;
        chat.sendTime = LocalDateTime.now();
        chat.userIdx = userIdx;
        return chat;
    }
}
