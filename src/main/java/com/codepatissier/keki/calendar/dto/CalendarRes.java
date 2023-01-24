package com.codepatissier.keki.calendar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CalendarRes {
    private String kindOfCalendar; // 캘린더 종류
    private String title;
    private String date;
    private String calDate;

    private List<CalendarHashTag> hashTags;
}
