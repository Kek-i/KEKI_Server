package com.codepatissier.keki.order.service;

import com.codepatissier.keki.common.BaseException;

import com.codepatissier.keki.common.Role;
import com.codepatissier.keki.order.dto.*;

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
import java.util.Optional;
import java.util.stream.Collectors;

import static com.codepatissier.keki.common.BaseResponseStatus.*;
import static com.codepatissier.keki.common.Constant.ACTIVE_STATUS;
import static com.codepatissier.keki.common.Role.*;
import static com.codepatissier.keki.order.entity.OrderStatus.*;

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


    // [판매자] 주문 상태 변경
    public void changeOrderStatus(Long userIdx, PatchOrderStatusReq patchOrderStatusReq) throws BaseException{
        User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_USER_AND_STATUS));
        Order order = orderRepository.findById(patchOrderStatusReq.getOrderIdx()).orElseThrow(() -> new BaseException(INVALID_ORDER_IDX));

        if(!order.getStore().getUser().equals(user)) throw new BaseException(NO_MATCH_ORDER_USER);
        order.changeOrderStatus(getOrderStatusByName(patchOrderStatusReq.getOrderStatus()));
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

    // 주문 내역 조회
    public GetOrderHistoryRes getOrderHistory(Long userIdx, GetOrderHistoryReq orderStatusReq) throws BaseException {
        User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        Role role = getRoleByName(user.getRole());
        OrderStatus orderStatus = getOrderStatusByName(orderStatusReq.getOrderStatus());
        Optional<List<Order>> orders = null;
        List<GetOrderHistory> orderHistories = null;
        NumOfOrder numOfOrder = getCountByOrderStatus(user);

        if (role == CUSTOMER) {
            if (orderStatus == null) orders = orderRepository.findAllByUserOrderByPickupDateDesc(user);
            else orders = orderRepository.findAllByUserAndOrderStatusEqualsOrderByPickupDateDesc(user, orderStatus);
            orderHistories = orders.orElseThrow(null).stream().map(oH -> new GetOrderHistory(oH.getOrderIdx(), oH.getStore().getUser().getNickname(), oH.getStore().getUser().getProfileImg(),
                    oH.getTotalPrice(), oH.getDessert().getDessertName(), oH.getPickupDate())).collect(Collectors.toList());
        } else if (role == STORE) {
            if (orderStatus == null) orders = orderRepository.findAllByStore_UserOrderByPickupDateDesc(user);
            else orders = orderRepository.findAllByStore_UserAndOrderStatusEqualsOrderByPickupDateDesc(user, orderStatus);
            orderHistories = orders.orElseThrow(null).stream().map(oH -> new GetOrderHistory(oH.getOrderIdx(), oH.getUser().getNickname(), oH.getUser().getProfileImg(),
                    oH.getTotalPrice(), oH.getDessert().getDessertName(), oH.getPickupDate())).collect(Collectors.toList());
        } else throw new BaseException(INVALID_USER_IDX);

        return new GetOrderHistoryRes(numOfOrder.getOrderWaiting(), numOfOrder.getProgressing(), numOfOrder.getPickupWaiting(), numOfOrder.getAllOrderHistory(), orderHistories);
    }

    // 주문 상태 별 주문 수
    private NumOfOrder getCountByOrderStatus(User user) throws BaseException{
        NumOfOrder numOfOrder = new NumOfOrder();
        if (getRoleByName(user.getRole()) == CUSTOMER) {
            numOfOrder.setOrderWaiting(orderRepository.countByUserAndOrderStatusEquals(user, ORDER_WAITING));
            numOfOrder.setProgressing(orderRepository.countByUserAndOrderStatusEquals(user, PROGRESSING));
            numOfOrder.setPickupWaiting(orderRepository.countByUserAndOrderStatusEquals(user, PICKUP_WAITING));
            numOfOrder.setAllOrderHistory(orderRepository.countAllByUser(user));
        } else if (getRoleByName(user.getRole()) == STORE) {
            numOfOrder.setOrderWaiting(orderRepository.countByStore_UserAndOrderStatusEquals(user, ORDER_WAITING));
            numOfOrder.setProgressing(orderRepository.countByStore_UserAndOrderStatusEquals(user, PROGRESSING));
            numOfOrder.setPickupWaiting(orderRepository.countByStore_UserAndOrderStatusEquals(user, PICKUP_WAITING));
            numOfOrder.setAllOrderHistory(orderRepository.countAllByStore_User(user));
        } else throw new BaseException(INVALID_USER_IDX);
        return numOfOrder;
    }
}
