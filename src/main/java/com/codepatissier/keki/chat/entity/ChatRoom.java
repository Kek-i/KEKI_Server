package com.codepatissier.keki.chat.entity;

import com.codepatissier.keki.common.BaseEntity;
import com.codepatissier.keki.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String chatRoomId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "storeUserIdx")
    private User store;

    @ManyToOne
    @JoinColumn(nullable = false, name = "userIdx")
    private User user;

    public static ChatRoom create(User store, User user){
        ChatRoom room = new ChatRoom();
        room.chatRoomId = UUID.randomUUID().toString();
        room.store = store;
        room.user = user;
        return room;
    }
}
