package com.codepatissier.keki.chat.entity;


import com.codepatissier.keki.common.BaseEntity;
import com.codepatissier.keki.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomIdx;

    @ManyToOne
    @JoinColumn(nullable = false, name = "storeUserIdx")
    private User store;

    @ManyToOne
    @JoinColumn(nullable = false, name = "userIdx")
    private User user;

    private Set<WebSocketSession> sessions = new HashSet<>();

    public static ChatRoom create(User store, User user){
        ChatRoom room = new ChatRoom();

        room.chatRoomIdx = Long.valueOf(UUID.randomUUID().toString());
        room.store = store;
        room.user = user;
        return room;
    }
}
