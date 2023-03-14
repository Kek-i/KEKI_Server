package com.codepatissier.keki.order.dto;

import com.codepatissier.keki.dessert.dto.OptionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetStoreDessertsAndOptions {
    Long storeIdx;
    String storeName;
    String accountName;
    String storeAccount;
    Long dessertIdx;
    String dessertName;
    Integer dessertPrice;
    List<OptionDTO> options;
}
