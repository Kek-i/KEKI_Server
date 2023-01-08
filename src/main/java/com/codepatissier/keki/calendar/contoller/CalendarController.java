package com.codepatissier.keki.calendar.contoller;

import com.codepatissier.keki.calendar.service.CalendarService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "calendars", description = "기념일 API")
@RestController
@RequestMapping(value = "/calendars")
public class CalendarController {
    private CalendarService calendarService;
    public CalendarController(CalendarService calendarService){
        this.calendarService = calendarService;
    }

}
