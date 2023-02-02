package com.codepatissier.keki.store.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class PostStoreRes {
    private final String accessToken;
    private final String refreshToken;
    private String role;

    @Builder
    public PostStoreRes(String accessToken, String refreshToken, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
    }
}