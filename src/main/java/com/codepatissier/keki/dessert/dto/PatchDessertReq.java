package com.codepatissier.keki.dessert.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PatchDessertReq {
    @NotNull
    private String dessertImg;
    @NotNull
    private String dessertName;
    @NotNull
    private Integer dessertPrice;
    @NotNull
    private String dessertDescription;
}