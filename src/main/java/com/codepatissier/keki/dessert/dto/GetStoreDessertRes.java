package com.codepatissier.keki.dessert.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetStoreDessertRes {
    private String dessertImg;
    private String dessertName;
    private Integer dessertPrice;
    private String dessertDescription;
}