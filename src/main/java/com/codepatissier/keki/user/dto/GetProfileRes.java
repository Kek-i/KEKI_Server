package com.codepatissier.keki.user.dto;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GetProfileRes {
    private String nickname;
    private String profileImg;


    public GetProfileRes(String nickname, String profileImg) {
        this.nickname = nickname;
        this.profileImg = profileImg;
    }
}
