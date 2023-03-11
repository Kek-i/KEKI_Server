package com.codepatissier.keki.order.service;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.order.dto.PatchOrderStatusReq;
import com.codepatissier.keki.order.entity.Order;
import com.codepatissier.keki.order.entity.OrderStatus;
import com.codepatissier.keki.order.repository.OrderRepository;
import com.codepatissier.keki.user.entity.User;
import com.codepatissier.keki.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.codepatissier.keki.common.BaseResponseStatus.*;
import static com.codepatissier.keki.common.Constant.ACTIVE_STATUS;
import static com.codepatissier.keki.order.entity.OrderStatus.getOrderStatusByName;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // 주문 취소
    public void cancelOrder(Long userIdx, Long orderIdx) throws BaseException{
        User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_USER_AND_STATUS));
        Order order = orderRepository.findById(orderIdx).orElseThrow(() -> new BaseException(INVALID_ORDER_IDX));

        if(!order.getUser().equals(user)) throw new BaseException(NO_MATCH_ORDER_USER);
        if(!order.getOrderStatus().equals(OrderStatus.ORDER_WAITING)) throw new BaseException(NO_MATCH_ORDER_STATUS);
        order.cancelOrder();
        orderRepository.save(order);
    }

    // [판매자] 주문 상태 변경
    public void changeOrderStatus(Long userIdx, PatchOrderStatusReq patchOrderStatusReq) throws BaseException{
        User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_USER_AND_STATUS));
        Order order = orderRepository.findById(patchOrderStatusReq.getOrderIdx()).orElseThrow(() -> new BaseException(INVALID_ORDER_IDX));

        if(!order.getStore().getUser().equals(user)) throw new BaseException(NO_MATCH_ORDER_USER);
        order.changeOrderStatus(getOrderStatusByName(patchOrderStatusReq.getOrderStatus()));
        orderRepository.save(order);

    }
}
