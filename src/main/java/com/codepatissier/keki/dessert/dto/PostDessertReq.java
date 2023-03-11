package com.codepatissier.keki.dessert.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
public class PostDessertReq {
    @NotBlank(message = "디저트 이름은 필수 항목입니다.")
    private String dessertName;
    @NotNull(message = "디저트 가격은 필수 항목입니다.")
    @PositiveOrZero(message = "디저트 가격은 0원 이상이어야 합니다.")
    private Integer dessertPrice;
    @NotBlank(message = "디저트 설명은 필수 항목입니다.")
    private String dessertDescription;
    @NotBlank(message = "디저트 이미지는 필수 항목입니다.")
    private String dessertImg;
    private List<OptionDTO> options;
}