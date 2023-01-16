package com.codepatissier.keki.calendar.repository;

import com.codepatissier.keki.calendar.entity.CalendarTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarTagRepository extends JpaRepository<CalendarTag, Long> {
}
