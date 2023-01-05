package com.example.keki.cs.entity;

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
public class Report extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportIdx;

    @OneToOne
    @JoinColumn(nullable = false, name = "postIdx")
    private Post post;

    @OneToOne
    @JoinColumn(nullable = false, name = "userIdx")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportCategory reportCategory;

    @Builder
    public Report(Post post, User user, ReportCategory reportCategory) {
        this.post = post;
        this.user = user;
        this.reportCategory = reportCategory;
    }
}
