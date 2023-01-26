package com.codepatissier.keki.dessert.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PostDessertReq {
    @NotBlank
    private String dessertName;
    @NotBlank
    private Integer dessertPrice;
    @NotBlank
    private String dessertDescription;
    @NotBlank
    private String dessertImg;
}