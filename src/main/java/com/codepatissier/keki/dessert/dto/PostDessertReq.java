package com.codepatissier.keki.dessert.dto;

import lombok.Data;

@Data
public class PostDessertReq {
    private String dessertName;
    private int dessertPrice;
    private String dessertDescription;
    private String dessertImg;
}