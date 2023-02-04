package com.codepatissier.keki.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetLikePostsRes {
    private List<GetPostRes> feeds;
    private LocalDateTime cursorDate;
    private boolean hasNext;
    private int numOfRows;
}
