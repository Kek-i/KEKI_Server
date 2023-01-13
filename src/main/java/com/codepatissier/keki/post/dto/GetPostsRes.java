package com.codepatissier.keki.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPostsRes {
    private Long postIdx;
    private String postImgUrl;

}
