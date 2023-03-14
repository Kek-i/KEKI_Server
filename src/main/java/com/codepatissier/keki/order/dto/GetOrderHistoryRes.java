package com.codepatissier.keki.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class GetOrderHistoryRes {
    private int orderWaiting;
    private int progressing;
    private int pickupWaiting;
    private int allOrderHistory;
    private List<GetOrderHistory> orderHistory;
}
