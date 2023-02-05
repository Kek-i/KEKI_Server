package com.codepatissier.keki.history.repository;

import com.codepatissier.keki.history.dto.QSearchRes;
import com.codepatissier.keki.history.dto.SearchRes;
import com.codepatissier.keki.history.entity.SearchHistory;
import com.codepatissier.keki.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.codepatissier.keki.history.entity.QSearchHistory.searchHistory;
import static com.codepatissier.keki.user.entity.QUser.user;

@RequiredArgsConstructor
public class SearchHistoryRepositoryImpl implements SearchHistoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    // 인기 검색어
    @Override
    public List<SearchRes> getPopularSearches() {
        return jpaQueryFactory.select(new QSearchRes(searchHistory.searchWord))
                .from(searchHistory)
                .groupBy(searchHistory.searchWord)
                .having(searchHistory.searchWord.count().goe(1L)) // 한 개 이상을 갖고 있는 경우
                .limit(5) // 인기 검색어 5개만 불러오기
                .orderBy(searchHistory.searchWord.count().desc())
                .fetch();
    }
    @Override
    public List<SearchHistory> getRecentSearches(User userEntity, String status) {
        return jpaQueryFactory.selectFrom(searchHistory)
                .where(user.eq(userEntity)
                        .and(searchHistory.status.eq(status)))
                .groupBy(searchHistory.searchWord)
                .limit(5)
                .orderBy(searchHistory.createdDate.desc())
                .fetch();
    }
}
