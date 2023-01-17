package com.codepatissier.keki.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPostsRes {
    private List<Feed> feeds;
    private Long cursorIdx;
    private boolean hasNext;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Feed {
        private Long postIdx;
        private String dessertName;
        private String description;
        private List<String> postImgUrls;
        private List<String> tags;
        private String brandName;
        private String storeProfileImg;
        private boolean like;
    }
}
