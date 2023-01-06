package com.example.keki.calendar;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CalendarCategory {
    D_DAY(1, "디데이"),
    DATE_COUNT(2, "날짜수"),
    EVERY_YEAR(3, "매년 반복");

    private int number;
    private String name;
    CalendarCategory(int number, String name){
        this.number = number;
        this.name = name;
    }

    public static CalendarCategory getCalendarCategoryByNum(int number){
        return Arrays.stream(CalendarCategory.values())
                .filter(r -> r.getNumber()==number)
                .findAny().orElse(null);
    }

    public static CalendarCategory getCalendarCategoryByName(String name){
        return Arrays.stream(CalendarCategory.values())
                .filter(r -> r.getName().equals(name))
                .findAny().orElse(null);
    }
}
