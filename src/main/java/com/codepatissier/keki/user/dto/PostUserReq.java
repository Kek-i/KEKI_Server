package com.codepatissier.keki.user.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PostUserReq {
    @NotBlank
    private String email;
    @NotBlank
    private String provider;
}
