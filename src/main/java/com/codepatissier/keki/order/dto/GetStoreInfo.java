package com.codepatissier.keki.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetStoreInfo {
    // 판매자 정보
    Long storeIdx;
    String storeName;
    String accountName;
    String storeAccount;
}
