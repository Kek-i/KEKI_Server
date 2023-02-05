package com.codepatissier.keki.history.repository;

import com.codepatissier.keki.history.entity.PostHistory;
import com.codepatissier.keki.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.codepatissier.keki.history.entity.QPostHistory.postHistory;
import static com.codepatissier.keki.post.entity.QPost.post;
import static com.codepatissier.keki.user.entity.QUser.user;

@RequiredArgsConstructor
public class PostHistoryRepositoryImpl implements PostHistoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    
    // 최근 본 게시물
    @Override
    public List<PostHistory> getRecentPosts(User userEntity) {
        return jpaQueryFactory.selectFrom(postHistory)
                .where(user.eq(userEntity))
                .groupBy(post)
                .orderBy(postHistory.createdDate.desc())
                .limit(5)
                .fetch();
    }
}
