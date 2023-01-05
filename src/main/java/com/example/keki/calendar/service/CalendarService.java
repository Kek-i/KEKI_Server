package com.example.keki.calendar.service;

import com.example.keki.calendar.repository.CalendarRepository;
import org.springframework.stereotype.Service;

@Service
public class CalendarService {
    private CalendarRepository calendarRepository;

    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }
}
