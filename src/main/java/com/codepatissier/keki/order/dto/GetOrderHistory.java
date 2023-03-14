package com.codepatissier.keki.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class GetOrderHistory {
    private Long orderIdx;
    private String userName;
    private String userProfileImg;
    private int totalPrice;
    private String dessertName;
    private LocalDateTime pickupDate;
}
