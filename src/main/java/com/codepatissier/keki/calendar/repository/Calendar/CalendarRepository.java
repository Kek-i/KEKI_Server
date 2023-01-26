package com.codepatissier.keki.calendar.repository.Calendar;

import com.codepatissier.keki.calendar.entity.Calendar;
import com.codepatissier.keki.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> , CalendarCustom{
    List<Calendar> findByUser(User user);
}