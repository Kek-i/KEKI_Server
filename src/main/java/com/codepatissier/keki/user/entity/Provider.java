package com.codepatissier.keki.user.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Provider {
    KAKAO(1, "카카오"),
    NAVER(2, "네이버"),
    GOOGLE(3, "구글"),
    ANONYMOUS(4,"비회원");

    private int number;
    private String name;
    Provider(int number, String name){
        this.number = number;
        this.name = name;
    }

    public static Provider getProviderByName(String name){
        return Arrays.stream(Provider.values())
                .filter(r -> r.getName().equals(name))
                .findAny().orElse(null);
    }
    }