package com.codepatissier.keki.post.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@DynamicInsert
public class PostImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postImgIdx;

    @ManyToOne
    @JoinColumn(nullable = false, name = "postIdx")
    private Post post;

    @Column(nullable = false, length = 300)
    private String imgUrl;

    @Builder
    public PostImg(Post post, String imgUrl) {
        this.post = post;
        this.imgUrl = imgUrl;
    }
}
