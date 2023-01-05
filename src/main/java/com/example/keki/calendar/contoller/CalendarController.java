package com.example.keki.calendar.contoller;

import com.example.keki.calendar.service.CalendarService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/calendars")
public class CalendarController {
    private CalendarService calendarService;
    public CalendarController(CalendarService calendarService){
        this.calendarService = calendarService;
    }

}
