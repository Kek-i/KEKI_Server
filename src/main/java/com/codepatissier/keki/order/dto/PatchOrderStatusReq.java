package com.codepatissier.keki.order.dto;

import lombok.Data;

@Data
public class PatchOrderStatusReq {
    private Long orderIdx;
    private String orderStatus;
}
