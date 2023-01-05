package com.example.keki.post.entity;

import com.example.keki.common.BaseEntity;
import com.example.keki.dessert.entity.Dessert;
import com.example.keki.user.entity.User;
import lombok.Builder;
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

    @ManyToOne
    @JoinColumn(nullable = false, name = "dessertIdx")
    private Dessert dessert;

    @ManyToOne
    @JoinColumn(nullable = false, name = "userIdx")
    private User user;

    @Column(nullable = false, length = 300)
    private String postDescription;

    @Builder
    public Post(Dessert dessert, User user, String postDescription) {
        this.dessert = dessert;
        this.user = user;
        this.postDescription = postDescription;
    }
}