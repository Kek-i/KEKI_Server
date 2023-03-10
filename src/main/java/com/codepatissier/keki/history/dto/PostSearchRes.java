package com.codepatissier.keki.history.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostSearchRes {
    private Long postIdx;
    private String postImgUrl;
}
