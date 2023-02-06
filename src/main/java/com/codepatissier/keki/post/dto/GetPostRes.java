package com.codepatissier.keki.post.dto;

import com.codepatissier.keki.post.entity.Post;
import com.codepatissier.keki.post.entity.PostImg;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.codepatissier.keki.common.Constant.ACTIVE_STATUS;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPostRes {
    private Long postIdx;
    private String dessertName;
    private int dessertPrice;
    private String description;
    private List<String> postImgUrls;
    private List<String> tags;
    private String storeName;
    private String storeProfileImg;
    private boolean like;

    public GetPostRes(Post post, boolean like){
        this.postIdx = post.getPostIdx();
        this.dessertName = post.getDessert().getDessertName();
        this.dessertPrice = post.getDessert().getDessertPrice();
        this.description = post.getPostDescription();
        this.postImgUrls = post.getImages().stream()
                .filter(postImg -> postImg.getStatus().equals(ACTIVE_STATUS))
                .map(PostImg::getImgUrl)
                .collect(Collectors.toList());
        this.tags = post.getTags().stream()
                .filter(postTag -> postTag.getStatus().equals(ACTIVE_STATUS))
                .map(postTag -> postTag.getTag().getTagName())
                .collect(Collectors.toList());
        this.storeName = post.getStore().getUser().getNickname();
        this.storeProfileImg = post.getStore().getUser().getProfileImg();
        this.like = like;
    }
}
