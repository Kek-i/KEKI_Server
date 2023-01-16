package com.codepatissier.keki.history.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class SearchRes {
    private String searchWord;

    @QueryProjection
    public SearchRes(String searchWord){
        this.searchWord = searchWord;
    }
}
