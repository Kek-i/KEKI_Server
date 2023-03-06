package com.codepatissier.keki.dessert.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDessertRes {
    private String nickname;
    private String dessertName;
    private Integer dessertPrice;
    private String dessertDescription;
    private List<Image> images;
    private List<Option> options;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Image {
        private Long imgIdx;
        private String imgUrl;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Option {
        private String optionDescription;
        private Integer optionPrice;
    }
}
