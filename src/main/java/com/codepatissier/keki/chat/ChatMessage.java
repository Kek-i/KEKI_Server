package com.codepatissier.keki.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {

    private String roomId;
    private String writer;
    private String message;
}