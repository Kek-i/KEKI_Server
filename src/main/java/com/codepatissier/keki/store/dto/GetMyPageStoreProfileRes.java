package com.codepatissier.keki.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetMyPageStoreProfileRes {
    private String storeImgUrl;
    private String nickname;
    private String address;
    private String introduction;
    private String orderUrl;
}