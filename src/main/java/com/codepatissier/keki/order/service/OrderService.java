package com.codepatissier.keki.order.service;

import com.codepatissier.keki.common.BaseException;

import com.codepatissier.keki.dessert.dto.OptionDTO;
import com.codepatissier.keki.dessert.entity.Dessert;
import com.codepatissier.keki.dessert.repository.DessertRepository;
import com.codepatissier.keki.dessert.repository.OptionRepository;
import com.codepatissier.keki.common.Role;
import com.codepatissier.keki.order.dto.*;

import com.codepatissier.keki.order.entity.Order;
import com.codepatissier.keki.order.entity.OrderStatus;
import com.codepatissier.keki.order.repository.OptionOrderRepository;
import com.codepatissier.keki.order.repository.OrderImgRepository;
import com.codepatissier.keki.order.repository.OrderRepository;
import com.codepatissier.keki.store.entity.Store;
import com.codepatissier.keki.store.repository.StoreRepository;
import com.codepatissier.keki.user.entity.User;
import com.codepatissier.keki.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final DessertRepository dessertRepository;
    private final StoreRepository storeRepository;
    private final OptionRepository optionRepository;

    // 주문 취소
    public void cancelOrder(Long userIdx, Long orderIdx) throws BaseException{
        User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        Order order = orderRepository.findById(orderIdx).orElseThrow(() -> new BaseException(INVALID_ORDER_IDX));

        if(!order.getUser().equals(user)) throw new BaseException(NO_MATCH_ORDER_USER);
        if(!order.getOrderStatus().equals(OrderStatus.ORDER_WAITING)) throw new BaseException(NO_MATCH_ORDER_STATUS);
        order.cancelOrder();
        orderRepository.save(order);
    }


    // [판매자] 주문 상태 변경
    public void changeOrderStatus(Long userIdx, PatchOrderStatusReq patchOrderStatusReq) throws BaseException{
        User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        Order order = orderRepository.findById(patchOrderStatusReq.getOrderIdx()).orElseThrow(() -> new BaseException(INVALID_ORDER_IDX));

        if(!order.getStore().getUser().equals(user)) throw new BaseException(NO_MATCH_ORDER_USER);
        order.changeOrderStatus(getOrderStatusByName(patchOrderStatusReq.getOrderStatus()));
        orderRepository.save(order);
    }

    // 주문 상세 return 값
    public GetOrder getOrderReturn(Long userIdx, Long orderIdx) throws BaseException{
        // TODO: 겹치는 부분이 3줄 이상인데 extract method 는 어떠신지?
        User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        Order order = orderRepository.findById(orderIdx).orElseThrow(() -> new BaseException(INVALID_ORDER_IDX));

        if(!order.getUser().equals(user)) throw new BaseException(NO_MATCH_ORDER_USER);
        return getOrder(order);

    }

    // 주문 상세 조회
    private GetOrder getOrder(Order order) {
        List<GetOrderImg> orderImgs = orderImgRepository.findByOrderAndStatusEquals(order, ACTIVE_STATUS).stream()
                .map(getOrder -> new GetOrderImg(getOrder.getOrderImgIdx(), getOrder.getImgUrl())).collect(Collectors.toList());
        List<GetOptionOrder> optionOrders = optionOrderRepository.findByOrderAndStatusEquals(order, ACTIVE_STATUS).stream()
                .map(getOptionOrder -> new GetOptionOrder(getOptionOrder.getOption().getOptionIdx(), getOptionOrder.getOption().getDescription(), getOptionOrder.getOption().getPrice())).collect(Collectors.toList());

        // TODO: 아직 판매자 계좌 번호 저장 이전
        return new GetOrder(order.getOrderIdx(), order.getOrderStatus().getName(), order.getDessert().getDessertIdx(), order.getDessert().getDessertName(),
                order.getDessert().getDessertPrice(), order.getExtraPrice(), order.getTotalPrice(), order.getRequest(), order.getPickupDate(), order.getStore().getStoreIdx(), order.getStore().getUser().getNickname(), null, order.getStore().getAddress(), orderImgs, optionOrders);
    }

    // 주문 조회
    public GetOrderStore getStoreDessertsAndOptions(Long storeIdx) throws BaseException{
        Store store = this.storeRepository.findByStoreIdxAndStatus(storeIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_STORE_IDX));
        // TODO: 아직 판매자 계좌 번호 저장 이전
        return new GetOrderStore(store.getStoreIdx(), store.getUser().getNickname(),
                null, store.getAddress(),getStoreDessertAndOptionList(store));
     }

     // 판매자 디저트 + 옵션 불러오기
    private List<GetStoreDessertAndOptions> getStoreDessertAndOptionList(Store store) throws BaseException{
        List<Dessert> desserts = this.dessertRepository.findByStoreAndStatusOrderByDessertIdx(store, ACTIVE_STATUS);
        List<GetStoreDessertAndOptions> dessertsAndOptions = new ArrayList<>();
        // 디저트 별의 option 들을 찾아서 한번에 저장하는 것이 필요함.
        for (Dessert dessert: desserts){
            dessertsAndOptions.add(new GetStoreDessertAndOptions(dessert.getDessertIdx(), dessert.getDessertName(), dessert.getDessertPrice(),
                    this.optionRepository.findByDessertAndStatusOrderByOptionIdx(dessert, ACTIVE_STATUS).stream()
                            .map(option -> new OptionDTO(option.getOptionIdx(), option.getDescription(), option.getPrice())).collect(Collectors.toList())));
        }
        return dessertsAndOptions;
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
    public NumOfOrder getCountByOrderStatus(User user) throws BaseException{
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

    // 주문 수정 조회
    public GetEditOrder getEditOrderView(Long orderIdx, Long userIdx) throws BaseException{
        User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        Order order = orderRepository.findById(orderIdx).orElseThrow(() -> new BaseException(INVALID_ORDER_IDX));
        if(!order.getUser().equals(user)) throw new BaseException(NO_MATCH_ORDER_USER);
        if(!order.getOrderStatus().equals(ORDER_WAITING)) throw new BaseException(NO_MATCH_ORDER_STATUS);

        return new GetEditOrder(this.getOrder(order), this.getStoreDessertAndOptionList(order.getStore()));
    }
}
