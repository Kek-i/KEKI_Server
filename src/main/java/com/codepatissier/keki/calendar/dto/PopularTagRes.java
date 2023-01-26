package com.codepatissier.keki.calendar.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter@Setter
public class PopularTagRes {
    private Long tagIdx;
    private String tagName;

    @QueryProjection
    public PopularTagRes(Long tagIdx, String tagName){
        this.tagIdx = tagIdx;
        this.tagName = tagName;
    }
}
