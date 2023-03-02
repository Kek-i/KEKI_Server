package com.codepatissier.keki.calendar.repository.CalendarTag;

import com.codepatissier.keki.calendar.dto.PopularTagRes;
import com.codepatissier.keki.calendar.dto.QPopularTagRes;
import com.codepatissier.keki.calendar.entity.Calendar;
import com.codepatissier.keki.common.Constant;
import com.codepatissier.keki.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.codepatissier.keki.calendar.entity.QCalendar.calendar;
import static com.codepatissier.keki.calendar.entity.QCalendarTag.calendarTag;
import static com.codepatissier.keki.common.Tag.QTag.tag;
import static com.codepatissier.keki.user.entity.QUser.user;

@RequiredArgsConstructor
public class CalendarTagRepositoryImpl implements CalendarTagCustom {
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 캘린더 별 인기 Tag 3개
     */
    @Override
    public List<PopularTagRes> getPopularCalendarTag() {
        return this.jpaQueryFactory.select(new QPopularTagRes(tag.tagIdx, tag.tagName))
                .from(tag)
                .leftJoin(calendarTag).on(calendarTag.tag.eq(tag))
                .where(tag.status.eq(Constant.ACTIVE_STATUS))
                .groupBy(tag.tagIdx)
                .orderBy(tag.tagIdx.count().desc())
                .limit(Constant.Home.HOME_RETURN_TAG_COUNT)
                .fetch();
    }

    /**
     * 사용자 별 인기 Tag 3개
     * 다음에 쓸수 있으니, 혹시 몰라서 지우지 않고 남겨둘게요!
     */
    @Override
    public List<PopularTagRes> getPopularCalendarTagByUser(User userEntity) {
        return this.jpaQueryFactory.select(new QPopularTagRes(tag.tagIdx, tag.tagName))
                .from(tag)
                .leftJoin(calendarTag).on(tag.eq(calendarTag.tag))
                .leftJoin(calendar).on(calendar.eq(calendarTag.calendar))
                .leftJoin(user).on(calendar.user.eq(user))
                .where(user.userIdx.eq(userEntity.getUserIdx()))
                .where(tag.status.eq(Constant.ACTIVE_STATUS))
                .where(calendarTag.status.eq(Constant.ACTIVE_STATUS))
                .groupBy(tag.tagIdx)
                .orderBy(tag.tagIdx.count().desc())
                .limit(Constant.Home.HOME_RETURN_TAG_COUNT)
                .fetch();
    }

}
