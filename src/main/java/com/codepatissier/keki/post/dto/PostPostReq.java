package com.codepatissier.keki.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostPostReq {
    private Long dessertIdx;
    private String description;
    private List<String> postImgUrls;
    private List<String> tags;
}
