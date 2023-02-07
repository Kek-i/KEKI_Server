package com.codepatissier.keki.post.repository;

import com.codepatissier.keki.post.entity.Post;
import com.querydsl.jpa.impl.JPAQuery;
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
    public List<Post> getPopularSortSearchPosts(String word, Pageable page) {
        return getPopularSortSearchPostsQuery(word)
                .limit(page.getPageSize())
                .fetch();
    }

    @Override
    public List<Post> getPopularSortSearchPostsWithCursor(String word, Long cursorIdx, Long cursorPopularNum, Pageable page) {
        return getPopularSortSearchPostsQuery(word)
                .having(postHistory.post.postIdx.count().loe(cursorPopularNum),
                        postHistory.post.postIdx.count().eq(cursorPopularNum).and(post.postIdx.goe(cursorIdx - 1L)).not())
                .limit(page.getPageSize())
                .fetch();
    }

    private JPAQuery<Post> getPopularSortSearchPostsQuery(String word) {
        return jpaQueryFactory.selectFrom(post)
                .leftJoin(postHistory).on(post.eq(postHistory.post))
                .where(post.dessert.dessertName.contains(word),
                        post.status.eq(ACTIVE_STATUS))
                .groupBy(post)
                .orderBy(postHistory.post.postIdx.count().desc(), post.postIdx.desc());
    }

    @Override
    public boolean existNextByPopularAndPostIdx(String word, Long cursorIdx, Long cursorPopularNum) {
        return getPopularSortSearchPostsQuery(word)
                .having(postHistory.post.postIdx.count().loe(cursorPopularNum),
                        postHistory.post.postIdx.count().eq(cursorPopularNum).and(post.postIdx.goe(cursorIdx - 1L)).not())
                .fetchFirst() != null;
    }

    @Override
    public List<Post> getLowPriceSortSearchPostsWithCursor(String word, Long cursorIdx, int cursorPrice, Pageable page) {
        return getLowPriceSortSearchPostsQuery(word, cursorIdx, cursorPrice)
                .limit(page.getPageSize())
                .fetch();
    }

    private JPAQuery<Post> getLowPriceSortSearchPostsQuery(String word, Long cursorIdx, int cursorPrice) {
        return jpaQueryFactory.selectFrom(post)
                .where(post.dessert.dessertName.contains(word),
                        post.status.eq(ACTIVE_STATUS),
                        post.dessert.dessertPrice.goe(cursorPrice),
                        post.dessert.dessertPrice.eq(cursorPrice).and(post.postIdx.goe(cursorIdx - 1L)).not())
                .groupBy(post)
                .orderBy(post.dessert.dessertPrice.asc(), post.postIdx.desc());
    }

    @Override
    public boolean existNextByPriceAndPostIdx(String word, Long cursorIdx, int cursorPrice) {
        return getLowPriceSortSearchPostsQuery(word, cursorIdx, cursorPrice)
                .fetchFirst() != null;
    }
}
