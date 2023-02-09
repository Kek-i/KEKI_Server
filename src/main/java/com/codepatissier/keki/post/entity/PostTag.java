package com.codepatissier.keki.post.entity;

import com.codepatissier.keki.common.BaseEntity;
import com.codepatissier.keki.common.Tag.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
@SQLDelete(sql = "UPDATE post_tag SET status = 'inactive' WHERE post_tag_idx = ?")
public class PostTag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postTagIdx;

    @ManyToOne
    @JoinColumn(nullable = false, name = "postIdx")
    private Post post;

    @ManyToOne
    @JoinColumn(nullable = false, name = "tagIdx")
    private Tag tag;

    @Builder
    public PostTag(Post post, Tag tag){
        this.post = post;
        this.tag = tag;
    }

}
