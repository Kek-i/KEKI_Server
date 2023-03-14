package com.codepatissier.keki.order.controller;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;

import com.codepatissier.keki.order.dto.GetStoreDessertsAndOptions;
import com.codepatissier.keki.order.dto.PatchOrderStatusReq;
import com.codepatissier.keki.order.entity.OrderStatus;

import com.codepatissier.keki.order.dto.GetOrder;

import com.codepatissier.keki.order.service.OrderService;
import com.codepatissier.keki.user.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Objects;

import static com.codepatissier.keki.common.BaseResponseStatus.*;
import static com.codepatissier.keki.order.entity.OrderStatus.getOrderStatusByName;

@SecurityRequirement(name = "Bearer")
@Tag(name = "orders", description = "주문 API")
@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
public class OrderController {

    private final AuthService authService;
    private final OrderService orderService;


    /**
     * [구매자] 주문 상세 조회 API
     * @param orderIdx
     * @return GetOrder
     */
    @GetMapping("{orderIdx}")
    public BaseResponse<GetOrder> getOrder(@PathVariable("orderIdx") Long orderIdx){
        try {
            return new BaseResponse<>(orderService.getOrder(authService.getUserIdx(), orderIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 주문 취소 API
     * @param orderIdx
     */
    @PatchMapping("/cancel/{orderIdx}")
    public BaseResponse<?> cancelOrder(@PathVariable("orderIdx") Long orderIdx) {
        try{
            orderService.cancelOrder(authService.getUserIdx(), orderIdx);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * [판매자] 주문 상태 변경
     * @param patchOrderStatusReq(orderIdx, orderStatus)
     */
    @PatchMapping("/status")
    public BaseResponse<?> changeOrderStatus(PatchOrderStatusReq patchOrderStatusReq) {
        try{
            if(Objects.isNull(patchOrderStatusReq.getOrderIdx())) throw new BaseException(NULL_ORDER_IDX);
            String orderStatus = patchOrderStatusReq.getOrderStatus();
            if(!StringUtils.hasText(orderStatus)) throw new BaseException(NULL_ORDER_STATUS);
            if(Objects.isNull(getOrderStatusByName(orderStatus)) || getOrderStatusByName(orderStatus) == OrderStatus.CANCELED) throw new BaseException(INVALID_ORDER_STATUS);
            orderService.changeOrderStatus(authService.getUserIdx(), patchOrderStatusReq);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 주문 화면 조회
     */
    @GetMapping("/orders/view/{storeIdx}")
    public BaseResponse<List<GetStoreDessertsAndOptions>> getStoreDessertsAndOptions(@PathVariable("storeIdx") Long storeIdx){
        try{
            return new BaseResponse<>(this.orderService.getStoreDessertsAndOptions(storeIdx));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
