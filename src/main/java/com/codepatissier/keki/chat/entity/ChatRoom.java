package com.codepatissier.keki.chat.entity;


import com.codepatissier.keki.common.BaseEntity;
import com.codepatissier.keki.common.entityListener.DessertEntityListener;
import com.codepatissier.keki.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

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
}
