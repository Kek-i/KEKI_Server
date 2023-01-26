package com.codepatissier.keki.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class HomeTagRes {
    private Long tagIdx;
    private String tagName;
    private List<HomePostRes> homePostRes;
}
