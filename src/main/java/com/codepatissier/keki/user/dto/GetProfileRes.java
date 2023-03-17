package com.codepatissier.keki.user.dto;


import com.codepatissier.keki.order.dto.NumOfOrder;
import com.codepatissier.keki.user.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GetProfileRes {
    private String email;
    private String nickname;
    private String profileImg;
    private int orderWaiting;
    private int progressing;
    private int pickupWaiting;

    public GetProfileRes(User user, NumOfOrder numOfOrder) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImg = user.getProfileImg();
        this.orderWaiting = numOfOrder.getOrderWaiting();
        this.progressing = numOfOrder.getProgressing();
        this.pickupWaiting = numOfOrder.getPickupWaiting();
    }
}
