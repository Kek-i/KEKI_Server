package com.codepatissier.keki.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMakePostRes {
    private List<DessertsRes> desserts;
    private List<String> tags;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DessertsRes{
        private Long dessertIdx;
        private String dessertName;
    }
}

