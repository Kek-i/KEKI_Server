package com.codepatissier.keki.calendar.dto;

import com.codepatissier.keki.calendar.DateCountCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CalendarDateReturn {
    private int calDate;
    private DateCountCategory dateCountCategory;
}
