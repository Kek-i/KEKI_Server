package com.codepatissier.keki.calendar;

import lombok.Getter;

@Getter
public enum DateCountCategory {
    ONE_HUNDRED(100),
    TWO_HUNDRED(200),
    THREE_HUNDRED(300),
    ONE_YEAR(365),
    FOUR_HUNDRED(400),
    FIVE_HUNDRED(500),
    SIX_HUNDRED(600),
    SEVEN_HUNDRED(700),
    TWO_YEAR(730),
    EIGHT_HUNDRED(800),
    NINE_HUNDRED(900),
    ONE_THOUSAND(1000),
    THREE_YEAR(1095),
    FOUR_YEAR(1460),
    FIVE_YEAR(1825),
    THO_THOUSAND(2000);
    private int count;

    DateCountCategory(int count){
        this.count = count;
    }
}
