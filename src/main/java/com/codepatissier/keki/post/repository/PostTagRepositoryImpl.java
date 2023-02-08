package com.codepatissier.keki.post.repository;

import com.codepatissier.keki.common.Tag.Tag;
import com.codepatissier.keki.post.entity.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.codepatissier.keki.common.Constant.*;
import static com.codepatissier.keki.history.entity.QPostHistory.postHistory;
import static com.codepatissier.keki.post.entity.QPost.post;
import static com.codepatissier.keki.post.entity.QPostTag.postTag;


@RequiredArgsConstructor
public class PostTagRepositoryImpl implements PostTagCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getPopularSortTagPosts(Tag searchTag, Pageable page) {
        return jpaQueryFactory.select(post)
                .from(postTag)
                .leftJoin(post).on(post.eq(postTag.post))
                .leftJoin(postHistory).on(post.eq(postHistory.post))
                .where(postTag.tag.eq(searchTag),
                        postTag.status.eq(ACTIVE_STATUS),
                        post.status.eq(ACTIVE_STATUS))
                .groupBy(post)
                .limit(page.getPageSize())
                .orderBy(postHistory.post.postIdx.count().desc(), post.postIdx.desc())
                .fetch();
    }

    @Override
    public List<Post> getPopularSortTagPostsWithCursor(Tag searchTag, Long cursorIdx, Pageable page) {
        return jpaQueryFactory.select(post)
                .from(postTag)
                .leftJoin(post).on(post.eq(postTag.post))
                .leftJoin(postHistory).on(post.eq(postHistory.post))
                .where(postTag.tag.eq(searchTag),
                        postTag.status.eq(ACTIVE_STATUS),
                        post.status.eq(ACTIVE_STATUS),
                        post.postIdx.loe(cursorIdx-1L))
                .groupBy(post)
                .orderBy(postHistory.post.postIdx.count().desc(), post.postIdx.desc())
                .limit(page.getPageSize())
                .fetch();
    }

    @Override
    public List<Post> getLowPriceSortTagPosts(Tag searchTag, Pageable page) {
        return jpaQueryFactory.select(post)
                .from(postTag)
                .leftJoin(post).on(post.eq(postTag.post))
                .where(postTag.tag.eq(searchTag),
                        postTag.status.eq(ACTIVE_STATUS),
                        post.status.eq(ACTIVE_STATUS))
                .groupBy(post)
                .limit(page.getPageSize())
                .orderBy(post.dessert.dessertPrice.asc(), post.postIdx.desc())
                .fetch();
    }

    @Override
    public List<Post> getLowPriceSortTagPostsWithCursor(Tag searchTag, Long cursorIdx, Pageable page) {
        return jpaQueryFactory.select(post)
                .from(postTag)
                .leftJoin(post).on(post.eq(postTag.post))
                .where(postTag.tag.eq(searchTag),
                        postTag.status.eq(ACTIVE_STATUS),
                        post.status.eq(ACTIVE_STATUS),
                        post.postIdx.loe(cursorIdx-1L))
                .groupBy(post)
                .orderBy(post.dessert.dessertPrice.asc(), post.postIdx.desc())
                .limit(page.getPageSize())
                .fetch();
    }
}
