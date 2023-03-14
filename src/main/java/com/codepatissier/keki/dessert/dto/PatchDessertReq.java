package com.codepatissier.keki.dessert.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class PatchDessertReq {
    private String dessertImg;
    private String dessertName;
    private Integer dessertPrice;
    private String dessertDescription;
    private List<OptionDTO> options;
}