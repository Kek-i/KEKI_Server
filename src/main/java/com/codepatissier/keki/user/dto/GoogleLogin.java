package com.codepatissier.keki.user.dto;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class GoogleLogin extends DefaultApi20{

    protected GoogleLogin(){
    }

    private static class InstanceHolder{
        private static final GoogleLogin INSTANCE = new GoogleLogin();
    }

    public static GoogleLogin instance(){
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://www.googleapis.com/oauth2/v4/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth?access_type=offline&prompt=consent";
    }

}