package com.codepatissier.keki.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CalendarEditRes {
    private String kindOfCalendar; // 캘린더 종류
    private String title;
    private String date;

    private List<CalendarHashTag> hashTags;
    private List<CalendarHashTag> addHashTags;
}
