package com.codepatissier.keki.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPostsRes {
    private List<GetPostRes> feeds;
    private Long cursorIdx;
    private Integer cursorPrice;
    private Integer cursorPopularNum;
    private boolean hasNext;
    private int numOfRows;
}
