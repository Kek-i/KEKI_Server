package com.codepatissier.keki.store.dto;

import com.codepatissier.keki.order.dto.NumOfOrder;
import com.codepatissier.keki.user.entity.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class GetStoreMyPageRes {
    private String storeImgUrl;
    private String email;
    private String nickname;
    private int orderWaiting;
    private int progressing;
    private int pickupWaiting;

    public GetStoreMyPageRes(User user, NumOfOrder numOfOrder) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.storeImgUrl = user.getProfileImg();
        this.orderWaiting = numOfOrder.getOrderWaiting();
        this.progressing = numOfOrder.getProgressing();
        this.pickupWaiting = numOfOrder.getPickupWaiting();
    }
}
