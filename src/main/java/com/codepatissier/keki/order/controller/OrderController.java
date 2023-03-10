package com.codepatissier.keki.order.controller;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.order.dto.GetOrder;
import com.codepatissier.keki.order.entity.Order;
import com.codepatissier.keki.order.service.OrderService;
import com.codepatissier.keki.user.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.codepatissier.keki.common.BaseResponseStatus.SUCCESS;

@SecurityRequirement(name = "Bearer")
@Tag(name = "orders", description = "주문 API")
@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
public class OrderController {

    private final AuthService authService;
    private final OrderService orderService;


    /**
     * 주문 상세 조히 API
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
}
