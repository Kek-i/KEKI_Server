package com.codepatissier.keki.calendar.repository.Calendar;

import com.codepatissier.keki.calendar.dto.HomePostRes;
import com.codepatissier.keki.calendar.entity.Calendar;
import com.codepatissier.keki.user.entity.User;

import java.util.List;

public interface CalendarCustom {
   Calendar getRecentDateCalendar(User user);
   List<HomePostRes> getTagByPostLimit5(Long tagIdx);
}
