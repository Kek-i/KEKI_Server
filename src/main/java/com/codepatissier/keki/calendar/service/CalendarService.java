package com.codepatissier.keki.calendar.service;

import com.codepatissier.keki.calendar.repository.CalendarRepository;
import org.springframework.stereotype.Service;

@Service
public class CalendarService {
    private CalendarRepository calendarRepository;

    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }
}
