package com.codepatissier.keki.dessert.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetStoreDessertRes {
    private String nickname;
    private String dessertImg;
    private String dessertName;
    private Integer dessertPrice;
    private String dessertDescription;
    private List<OptionDTO> options;
}