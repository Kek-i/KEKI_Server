package com.codepatissier.keki.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NumOfOrder {
    private int orderWaiting;
    private int progressing;
    private int pickupWaiting;
    private int allOrderHistory;
}
