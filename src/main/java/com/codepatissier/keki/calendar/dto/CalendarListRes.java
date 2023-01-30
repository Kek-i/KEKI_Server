package com.codepatissier.keki.calendar.dto;

import com.codepatissier.keki.common.EmptyStringToNullConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Convert;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Convert(converter = EmptyStringToNullConverter.class)
public class CalendarListRes {
    private Long calendarIdx;
    private String title;
    private String date;
    private String calDate;
}
