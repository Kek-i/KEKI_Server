package com.codepatissier.keki.post.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum OrderStatus {
    ORDER_WAITING(1, "주문대기"),
    PROGRESSING(2, "제작중"),
    PICKUP_WAITING(3, "픽업대기"),
    COMPLETED(4, "픽업완료"),
    CANCEL(5, "주문취소");

    private int number;
    private String name;
    OrderStatus(int number, String name){
        this.number = number;
        this.name = name;
    }

    public static OrderStatus getOrderStatusByName(String name){
        return Arrays.stream(OrderStatus.values())
                .filter(r -> r.getName().equals(name))
                .findAny().orElse(null);
    }
}
