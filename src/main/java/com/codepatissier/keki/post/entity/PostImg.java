package com.codepatissier.keki.post.entity;

import com.codepatissier.keki.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class PostImg extends BaseEntity {
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
