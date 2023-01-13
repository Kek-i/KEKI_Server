package com.codepatissier.keki.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetStoreInfoRes {
    private String businessName;
    private String brandName;
    private String businessAddress;
    private String businessNumber;
}
