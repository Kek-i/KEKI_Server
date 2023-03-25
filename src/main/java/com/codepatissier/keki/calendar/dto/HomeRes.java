package com.codepatissier.keki.calendar.dto;

import com.codepatissier.keki.calendar.entity.Calendar;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class HomeRes {
    private Long userIdx;
    private String nickname;
    private String calendarTitle;
    private int calendarDate;
    private List<HomeTagRes> homeTagResList;
    @JsonIgnore
    private Calendar calendar;
}
