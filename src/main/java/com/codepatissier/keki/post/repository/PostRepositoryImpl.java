package com.codepatissier.keki.post.repository;

import com.codepatissier.keki.post.entity.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.codepatissier.keki.common.Constant.ACTIVE_STATUS;
import static com.codepatissier.keki.history.entity.QPostHistory.postHistory;
import static com.codepatissier.keki.post.entity.QPost.post;


@RequiredArgsConstructor
public class PostRepositoryImpl implements PostCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getPopularSortSearchPosts(String searchWord, Pageable page) {
        return jpaQueryFactory.select(post)
                .from(post, postHistory)
                .rightJoin(postHistory.post, post)
                .where(post.dessert.dessertName.contains(searchWord),
                        post.status.eq(ACTIVE_STATUS))
                .groupBy(post.postIdx)
                .limit(page.getPageSize())
                .orderBy(postHistory.post.postIdx.count().desc(), post.postIdx.desc())
                .fetch();
    }

    @Override
    public List<Post> getPopularSortSearchPostsWithCursor(String searchWord, Long cursorIdx, Pageable page) {
        return jpaQueryFactory.select(post)
                .from(post, postHistory)
                .rightJoin(postHistory.post, post)
                .where(post.dessert.dessertName.contains(searchWord),
                        post.status.eq(ACTIVE_STATUS),
                        post.postIdx.loe(cursorIdx-1L))
                .groupBy(post.postIdx)
                .limit(page.getPageSize())
                .orderBy(postHistory.post.postIdx.count().desc(), post.postIdx.desc())
                .fetch();
    }
}
