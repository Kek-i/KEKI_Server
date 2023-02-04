package com.codepatissier.keki.calendar.repository.CalendarTag;

import com.codepatissier.keki.calendar.entity.Calendar;
import com.codepatissier.keki.calendar.entity.CalendarTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarTagRepository extends JpaRepository<CalendarTag, Long>, CalendarTagCustom{
    List<CalendarTag> findByCalendarAndStatus(Calendar calendar, String activeStatus);
}
