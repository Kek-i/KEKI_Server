package com.codepatissier.keki.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostStoreReq {
    private String address;
    private String introduction;
    private String orderUrl;
    private String businessName;
    private String brandName;
    private String businessAddress;
    private String businessNumber;
}
