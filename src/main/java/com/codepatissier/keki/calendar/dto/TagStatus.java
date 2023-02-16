package com.codepatissier.keki.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class TagStatus {
    private String calendarHashTag;
    private Boolean status;
}
