package com.codepatissier.keki.user.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PostCustomerReq {
    private String nickname;
    private String profileImg;
}
