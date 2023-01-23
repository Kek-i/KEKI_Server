package com.codepatissier.keki.dessert.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetStoreDessertsRes {
    private List<Dessert> desserts;
    private Long cursorIdx;
    private boolean hasNext;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Dessert {
        private Long dessertIdx;
        private String dessertImgUrl;
        private String dessertName;
    }
}
