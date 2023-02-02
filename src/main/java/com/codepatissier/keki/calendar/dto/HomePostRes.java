package com.codepatissier.keki.calendar.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter@Setter
public class HomePostRes {
    private Long postIdx;
    private String storeTitle;
    private String postImgUrl;
    @QueryProjection
    public HomePostRes(Long postIdx, String storeTitle, String postImgUrl){
        this.postIdx = postIdx;
        this.storeTitle = storeTitle;
        this.postImgUrl = postImgUrl;
    }
}
