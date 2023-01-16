package com.codepatissier.keki.calendar.repository;

import com.codepatissier.keki.calendar.entity.Calendar;
import com.codepatissier.keki.calendar.entity.CalendarTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarTagRepository extends JpaRepository<CalendarTag, Long> {
    List<CalendarTag> findByCalendar(Calendar calendar);
}
