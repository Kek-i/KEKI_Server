package com.codepatissier.keki.calendar.repository.Calendar;

import com.codepatissier.keki.calendar.dto.HomePostRes;
import com.codepatissier.keki.calendar.dto.QHomePostRes;
import com.codepatissier.keki.calendar.entity.Calendar;
import com.codepatissier.keki.common.Constant;
import com.codepatissier.keki.user.entity.User;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.codepatissier.keki.calendar.entity.QCalendar.calendar;
import static com.codepatissier.keki.post.entity.QPost.post;
import static com.codepatissier.keki.post.entity.QPostImg.postImg;
import static com.codepatissier.keki.post.entity.QPostTag.postTag;

@RequiredArgsConstructor
public class CalendarRepositoryImpl implements CalendarCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Calendar getRecentDateCalendar(User userEntity) {
        LocalDate beginTimePath = LocalDate.now();
        return jpaQueryFactory.selectFrom(calendar)
                .where(calendar.calendarDate.after(beginTimePath))
                .where(calendar.user.userIdx.eq(userEntity.getUserIdx()))
                .where(calendar.status.eq(Constant.ACTIVE_STATUS))
                .orderBy(calendar.calendarDate.asc())
                .limit(1)
                .fetchFirst();
    }

    @Override
    public List<HomePostRes> getTagByPostLimit5(Long tagIdx) {
        return jpaQueryFactory.select(new QHomePostRes(post.postIdx, postImg.imgUrl))
                .from(postTag)
                .leftJoin(post).on(post.eq(postTag.post))
                .leftJoin(postImg).on(post.eq(postImg.post))
                .where(postTag.tag.tagIdx.eq(tagIdx))
                .where(post.status.eq(Constant.ACTIVE_STATUS))
                .groupBy(postTag.tag, post)
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc()) // 랜덤한 값을 불러오기
                .limit(Constant.Home.HOME_RETURN_POST_COUNT)
                .fetch();
    }
}
