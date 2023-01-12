package com.codepatissier.keki.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProfileRes {
    // 수정 필요) 사진, 가게 이름, 가게 설명
    private String introduction;
}
