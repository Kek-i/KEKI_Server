package com.codepatissier.keki.calendar;

import lombok.Getter;

@Getter
public enum DateCountCategory {
    ONE_HUNDRED(100, "100일"),
    TWO_HUNDRED(200, "200일"),
    THREE_HUNDRED(300, "200일"),
    ONE_YEAR(365, "1주년"),
    FOUR_HUNDRED(400, "400일"),
    FIVE_HUNDRED(500, "500일"),
    SIX_HUNDRED(600, "600일"),
    SEVEN_HUNDRED(700, "700일"),
    TWO_YEAR(730, "2주년"),
    EIGHT_HUNDRED(800, "800일"),
    NINE_HUNDRED(900, "900일"),
    ONE_THOUSAND(1000, "1000일"),
    THREE_YEAR(1095, "3주년"),
    FOUR_YEAR(1460, "4주년"),
    FIVE_YEAR(1825, "5주년"),
    THO_THOUSAND(2000, "200일");
    private int count;
    private String date;

    DateCountCategory(int count, String date){
        this.count = count;
        this.date = date;
    }
}
