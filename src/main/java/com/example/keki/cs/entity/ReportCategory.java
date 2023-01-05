package com.example.keki.cs.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ReportCategory {

    JUNK(1, "스팸홍보/도배"),
    ABUSE(2, "욕설/혐오/차별"),
    PORN(3, "음란물/유해한 정보"),
    FRAUD(4, "사기/불법정보"),
    NON_FIT(5, "게시글 성격에 부적절함");

    private int number;
    private String name;

    ReportCategory(int number, String name) {
        this.number = number;
        this.name = name;
    }

    public static ReportCategory getReportCategoryByName(String name){
        return Arrays.stream(ReportCategory.values())
                .filter(cs -> cs.getName().equals(name))
                .findAny().orElse(null);
    }
}
