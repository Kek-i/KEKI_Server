package com.codepatissier.keki.order.service;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.order.dto.GetOptionOrder;
import com.codepatissier.keki.order.dto.GetOrder;
import com.codepatissier.keki.order.dto.GetOrderImg;
import com.codepatissier.keki.order.entity.Order;
import com.codepatissier.keki.order.entity.OrderStatus;
import com.codepatissier.keki.order.repository.OptionOrderRepository;
import com.codepatissier.keki.order.repository.OrderImgRepository;
import com.codepatissier.keki.order.repository.OrderRepository;
import com.codepatissier.keki.user.entity.User;
import com.codepatissier.keki.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.codepatissier.keki.common.BaseResponseStatus.*;
import static com.codepatissier.keki.common.Constant.ACTIVE_STATUS;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderImgRepository orderImgRepository;
    private final OptionOrderRepository optionOrderRepository;

    // 주문 취소
    public void cancelOrder(Long userIdx, Long orderIdx) throws BaseException{
        User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_USER_AND_STATUS));
        Order order = orderRepository.findById(orderIdx).orElseThrow(() -> new BaseException(INVALID_ORDER_IDX));

        if(!order.getUser().equals(user)) throw new BaseException(NO_MATCH_ORDER_USER);
        if(!order.getOrderStatus().equals(OrderStatus.ORDER_WAITING)) throw new BaseException(NO_MATCH_ORDER_STATUS);
        order.cancelOrder();
        orderRepository.save(order);
    }

    public GetOrder getOrder(Long userIdx, Long orderIdx) throws BaseException{
        // TODO: 겹치는 부분이 3줄 이상인데 extract method 는 어떠신지?
        User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_USER_AND_STATUS));
        Order order = orderRepository.findById(orderIdx).orElseThrow(() -> new BaseException(INVALID_ORDER_IDX));

        if(!order.getUser().equals(user)) throw new BaseException(NO_MATCH_ORDER_USER);

        List<GetOrderImg> orderImgs = orderImgRepository.findByOrderAndStatusEquals(order, ACTIVE_STATUS).stream()
                .map(getOrder -> new GetOrderImg(getOrder.getOrderImgIdx(), getOrder.getImgUrl())).collect(Collectors.toList());
        List<GetOptionOrder> optionOrders = optionOrderRepository.findByOrderAndStatusEquals(order, ACTIVE_STATUS).stream()
                .map(getOptionOrder -> new GetOptionOrder(getOptionOrder.getOption().getOptionIdx(), getOptionOrder.getOption().getDescription(), getOptionOrder.getOption().getPrice())).collect(Collectors.toList());

        // TODO: 아직 판매자 계좌 번호는 DB에 없는 건가요?
        return new GetOrder(order.getOrderStatus().getName(), order.getDessert().getDessertName(),
                order.getDessert().getDessertPrice(), order.getExtraPrice(), order.getTotalPrice(), order.getRequest(), order.getPickupDate(), order.getStore().getStoreIdx(), order.getStore().getUser().getNickname(), null, order.getStore().getAddress(), orderImgs, optionOrders);

    }
}
