package com.example.keki.history.entity;

import com.example.keki.common.BaseEntity;
import com.example.keki.post.entity.Post;
import com.example.keki.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@DynamicInsert
@Getter
public class PostHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postHistoryIdx;

    @OneToOne
    @JoinColumn(nullable = false, name = "userIdx")
    private User user;

    @OneToOne
    @JoinColumn(nullable = false, name = "postIdx")
    private Post post;

    @Builder
    public PostHistory(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}
