package com.codepatissier.keki.user.dto;

import lombok.Data;

@Data
public class PatchProfileReq {
    private String nickname;
    private String profileImg;
}
