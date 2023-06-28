package com.codepatissier.keki.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    Long orderIdx;
    String orderStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDateTime orderDate;

    Long dessertIdx;
    String dessertName;

    // 주문 금액
    Integer dessertPrice;
    Integer optionPrice;
    Integer totalPrice;
    
    // 요청 사항
    String request;
    LocalDateTime pickupDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    GetStoreInfo storeInfo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    GetUserInfo userInfo;

    // 이미지, 옵션 추가
    List<GetOrderImg> orderImgs;
    List<GetOptionOrder> optionOrders;

}
