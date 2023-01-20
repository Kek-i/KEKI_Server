package com.codepatissier.keki.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class PostUserRes {
    private final String accessToken;
    private final String refreshToken;
    private String role;

    @Builder
    public PostUserRes(String accessToken, String refreshToken, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
    }
}
