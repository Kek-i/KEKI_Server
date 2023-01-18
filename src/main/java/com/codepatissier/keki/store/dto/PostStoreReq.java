package com.codepatissier.keki.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostStoreReq {
    @NotBlank
    private String nickname;
    private String storeImgUrl;
    @NotBlank
    private String address;
    private String introduction;
    private String orderUrl;
    @NotBlank
    private String businessName;
    @NotBlank
    private String brandName;
    @NotBlank
    private String businessAddress;
    @NotBlank
    private String businessNumber;
}
