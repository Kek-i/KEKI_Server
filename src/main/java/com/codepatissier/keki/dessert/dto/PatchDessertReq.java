package com.codepatissier.keki.dessert.dto;

import lombok.Data;

@Data
public class PatchDessertReq {
    private String dessertImg;
    private String dessertName;
    private Integer dessertPrice;
    private String dessertDescription;
}