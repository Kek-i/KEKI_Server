package com.codepatissier.keki.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderStore {
    Long storeIdx;
    String storeName;
    String accountName;
    String storeAccount;
    List<GetStoreDessertAndOptions> getStoreDessertsAndOptions;
}
