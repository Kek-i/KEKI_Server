package com.codepatissier.keki.dessert.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PostDessertReq {
    @NotBlank
    private String dessertName;
    @NotBlank
    private int dessertPrice;
    @NotBlank
    private String dessertDescription;
    @NotNull
    private String dessertImg;
}