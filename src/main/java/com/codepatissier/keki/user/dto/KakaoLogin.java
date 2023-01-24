package com.codepatissier.keki.user.dto;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class KakaoLogin extends DefaultApi20 {
    protected KakaoLogin() {
    }

    private static class InstanceHolder {
        private static final KakaoLogin INSTANCE = new KakaoLogin();
    }

    public static KakaoLogin instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://kauth.kakao.com/oauth/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://kauth.kakao.com/oauth/authorize";
    }
}
