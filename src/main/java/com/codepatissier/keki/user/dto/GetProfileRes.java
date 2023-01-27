package com.codepatissier.keki.user.dto;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GetProfileRes {
    private String email;
    private String nickname;
    private String profileImg;


    public GetProfileRes(String email, String nickname, String profileImg) {
        this.email = email;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }
}
