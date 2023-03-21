package com.codepatissier.keki.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetMyPageStoreProfileRes {
    private Long storeIdx;
    private String storeImgUrl;
    private String email;
    private String nickname;
    private String address;
    private String introduction;
    private String businessName;
    private String brandName;
    private String businessAddress;
    private String businessNumber;
}