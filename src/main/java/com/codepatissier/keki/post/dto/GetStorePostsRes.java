package com.codepatissier.keki.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetStorePostsRes {
    private List<Item> items;
    private Long cursorIdx;
    private boolean hasNext;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Item{
        private Long postIdx;
        private String postImgUrl;
    }
}
