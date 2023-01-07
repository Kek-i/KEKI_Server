package com.codepatissier.keki.cs.entity;

import com.codepatissier.keki.common.BaseEntity;
import com.codepatissier.keki.post.entity.Post;
import com.codepatissier.keki.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@DynamicInsert
@Getter
public class Hide extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hideIdx;

    @OneToOne
    @JoinColumn(nullable = false, name = "postIdx")
    private Post post;

    @OneToOne
    @JoinColumn(nullable = false, name = "userIdx")
    private User user;

    @Builder
    public Hide(Post post, User user) {
        this.post = post;
        this.user = user;
    }
}
