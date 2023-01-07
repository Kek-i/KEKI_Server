package com.codepatissier.keki.calendar.repository;

import com.codepatissier.keki.calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
}
