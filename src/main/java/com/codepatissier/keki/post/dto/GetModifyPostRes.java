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
public class GetModifyPostRes {
    private Long postIdx;
    private Long currentDessertIdx;
    private String currentDessertName;
    private String description;
    private List<String> postImgUrls;
    private List<String> currentTags;
    private List<DessertsRes> desserts;
    private List<String> tagCategories;

    public GetModifyPostRes(Post post, GetMakePostRes defaultData){
        this.postIdx = post.getPostIdx();
        this.currentDessertIdx = post.getDessert().getDessertIdx();
        this.currentDessertName = post.getDessert().getDessertName();
        this.description = post.getPostDescription();
        this.postImgUrls = post.getImages().stream()
                .filter(postImg -> postImg.getStatus().equals(ACTIVE_STATUS))
                .map(PostImg::getImgUrl)
                .collect(Collectors.toList());
        this.currentTags = post.getTags().stream()
                .filter(postTag -> postTag.getStatus().equals(ACTIVE_STATUS))
                .map(postTag -> postTag.getTag().getTagName())
                .collect(Collectors.toList());
        this.desserts = defaultData.getDesserts();
        this.tagCategories = defaultData.getTags();
    }
}
