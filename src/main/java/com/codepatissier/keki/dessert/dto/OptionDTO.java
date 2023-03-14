package com.codepatissier.keki.dessert.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OptionDTO {
    private Long optionIdx;
    private String optionDescription;
    private Integer optionPrice;
}