package com.codepatissier.keki.post.dto;

import com.codepatissier.keki.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetLikePostRes {
    private Long postIdx;
    private String dessertName;
    private int dessertPrice;
    private String postImgUrl;

    public GetLikePostRes(Post post){
        this.postIdx = post.getPostIdx();
        this.dessertName = post.getDessert().getDessertName();
        this.dessertPrice = post.getDessert().getDessertPrice();
        this.postImgUrl = post.getImages().get(0).getImgUrl();
    }
}
