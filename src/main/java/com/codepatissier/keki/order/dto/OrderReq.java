package com.codepatissier.keki.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderReq {
    private String customerName;
    private String customerPhone;
    private Long dessertIdx;
    private List<Long> options;
    private LocalDateTime pickupDate;
    private String request;
    private List<String> imgUrls;
    private Integer extraPrice;
    private Integer totalPrice;
}
