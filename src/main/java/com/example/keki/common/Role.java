package com.example.keki.common;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Role {
    CUSTOMER(1, "구매자"),
    STORE(2, "판매자");

    private int number;
    private String name;
    Role(int number, String name){
        this.number = number;
        this.name = name;
    }

    public static Role getContractStatusByNum(int number){
        return Arrays.stream(Role.values())
                .filter(r -> r.getNumber()==number)
                .findAny().orElse(null);
    }

    public static Role getContractStatusByName(String name){
        return Arrays.stream(Role.values())
                .filter(r -> r.getName().equals(name))
                .findAny().orElse(null);
    }
}
