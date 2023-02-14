package com.codepatissier.keki.post.repository;

import com.codepatissier.keki.common.Tag.Tag;
import com.codepatissier.keki.post.entity.Post;
import com.querydsl.jpa.impl.JPAQuery;
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
    public List<Post> getPopularSortTagPosts(Tag tag, Pageable page) {
        return getPopularSortTagPostsQuery(tag)
                .limit(page.getPageSize())
                .fetch();
    }

    @Override
    public List<Post> getPopularSortTagPostsWithCursor(Tag tag, Long cursorIdx, Long cursorPopularNum, Pageable page) {
        return getPopularSortTagPostsQuery(tag)
                .having(postHistory.post.postIdx.count().loe(cursorPopularNum),
                        postHistory.post.postIdx.count().eq(cursorPopularNum).and(post.postIdx.goe(cursorIdx - 1L)).not())
                .limit(page.getPageSize())
                .fetch();
    }

    @Override
    public boolean existNextByPopularAndPostIdx(Tag tag, Long cursorIdx, Long cursorPopularNum) {
        return getPopularSortTagPostsQuery(tag)
                .having(postHistory.post.postIdx.count().loe(cursorPopularNum),
                        postHistory.post.postIdx.count().eq(cursorPopularNum).and(post.postIdx.goe(cursorIdx - 1L)).not())
                .fetchFirst() != null;
    }

    private JPAQuery<Post> getPopularSortTagPostsQuery(Tag tag) {
        return jpaQueryFactory.select(post)
                .from(postTag)
                .leftJoin(post).on(post.eq(postTag.post))
                .leftJoin(postHistory).on(post.eq(postHistory.post))
                .where(postTag.tag.eq(tag),
                        postTag.status.eq(ACTIVE_STATUS),
                        post.status.eq(ACTIVE_STATUS))
                .groupBy(post)
                .orderBy(postHistory.post.postIdx.count().desc(), post.postIdx.desc());
    }

    @Override
    public List<Post> getLowPriceSortTagPosts(Tag tag, Pageable page) {
        return getLowPriceSortTagPostsQuery(tag)
                .limit(page.getPageSize())
                .fetch();
    }

    @Override
    public List<Post> getLowPriceSortTagPostsWithCursor(Tag tag, Long cursorIdx, int cursorPrice, Pageable page) {
        return getLowPriceSortTagPostsQuery(tag)
                .where(post.dessert.dessertPrice.goe(cursorPrice),
                        post.dessert.dessertPrice.eq(cursorPrice).and(post.postIdx.goe(cursorIdx - 1L)).not())
                .limit(page.getPageSize())
                .fetch();
    }

    @Override
    public boolean existNextByPriceAndPostIdx(Tag tag, Long cursorIdx, int cursorPrice) {
        return getLowPriceSortTagPostsQuery(tag)
                .where(post.dessert.dessertPrice.goe(cursorPrice),
                        post.dessert.dessertPrice.eq(cursorPrice).and(post.postIdx.goe(cursorIdx - 1L)).not())
                .fetchFirst() != null;
    }

    private JPAQuery<Post> getLowPriceSortTagPostsQuery(Tag tag) {
        return jpaQueryFactory.select(post)
                .from(postTag)
                .leftJoin(post).on(post.eq(postTag.post))
                .where(postTag.tag.eq(tag),
                        postTag.status.eq(ACTIVE_STATUS),
                        post.status.eq(ACTIVE_STATUS))
                .groupBy(post)
                .orderBy(post.dessert.dessertPrice.asc(), post.postIdx.desc());
    }
}
