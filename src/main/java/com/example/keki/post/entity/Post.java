package com.example.keki.post.entity;

import com.example.keki.common.BaseEntity;
import com.example.keki.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postIdx;

    //dessertIdx 추가 필요

    @ManyToOne
    @JoinColumn(nullable = false, name = "userIdx")
    private User userIdx;

    @Column(nullable = false, length = 300)
    private String postDescription;
}
