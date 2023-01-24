package com.codepatissier.keki.user.dto;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class NaverLogin extends DefaultApi20{

    protected NaverLogin(){
    }

    private static class InstanceHolder{
        private static final NaverLogin INSTANCE = new NaverLogin();
    }

    public static NaverLogin instance(){
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://nid.naver.com/oauth2.0/authorize";
    }
}
