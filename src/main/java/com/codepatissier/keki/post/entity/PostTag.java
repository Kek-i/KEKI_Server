package com.codepatissier.keki.post.entity;

import com.codepatissier.keki.common.Tag;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@DynamicInsert
public class PostTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postTagIdx;

    @ManyToOne
    @JoinColumn(nullable = false, name = "postIdx")
    private Post post;

    @ManyToOne
    @JoinColumn(nullable = false, name = "tagIdx")
    private Tag tag;


}
