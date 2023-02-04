package com.codepatissier.keki.calendar.dto;

import com.codepatissier.keki.common.EmptyStringToNullConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Convert;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Convert(converter = EmptyStringToNullConverter.class)
public class CalendarReq {
    private String kindOfCalendar; // 캘린더 종류
    private String title;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") //2022-01-01
    private LocalDate date;

    private List<CalendarHashTag> hashTags;
}
