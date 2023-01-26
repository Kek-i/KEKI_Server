package com.codepatissier.keki.calendar.repository.CalendarTag;

import com.codepatissier.keki.calendar.dto.PopularTagRes;
import com.codepatissier.keki.user.entity.User;

import java.util.List;

public interface CalendarTagCustom {
    List<PopularTagRes> getPopularCalendarTag();
    List<PopularTagRes> getPopularCalendarTagByUser(User user);
}
