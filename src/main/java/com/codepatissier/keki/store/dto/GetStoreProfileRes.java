package com.codepatissier.keki.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetStoreProfileRes {
    private String nickname;
    private String storeImgUrl;
    private String introduction;
}
