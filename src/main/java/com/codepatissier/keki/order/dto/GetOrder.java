package com.codepatissier.keki.order.dto;

import com.codepatissier.keki.order.entity.OrderStatus;
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
public class GetOrder {
    String orderStatus;
    String dessertName;

    // 주문 금액
    Integer dessertPrice;
    Integer optionPrice;
    Integer totalPrice;
    
    // 요청 사항
    String request;
    LocalDateTime pickupDate;

    // 판매자 정보
    Long storeIdx;
    String storeName;
    String accountName;
    String storeAccount;

    // 이미지, 옵션 추가
    List<GetOrderImg> orderImgs;
    List<GetOptionOrder> optionOrders;

}
