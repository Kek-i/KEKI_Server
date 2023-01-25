package com.codepatissier.keki.dessert.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PatchDessertReq {
    @NotBlank
    private String dessertImg;
    @NotBlank
    private String dessertName;
    @NotBlank
    private Integer dessertPrice;
    @NotBlank
    private String dessertDescription;
}