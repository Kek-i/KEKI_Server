package com.codepatissier.keki.post.controller;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.post.dto.GetPostsRes;
import com.codepatissier.keki.post.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import static com.codepatissier.keki.common.BaseResponseStatus.*;
import static com.codepatissier.keki.common.Constant.Posts.*;

@Tag(name = "posts", description = "피드 API")
@RestController
@RequestMapping(value = "/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    /**
     * 피드 목록 조회(최초 호출)
     * [POST] /posts? storeIdx= &searchTag= &searchWord= &size=
     * or
     * 피드 목록 조회(다음 호출)
     * [POST] /posts? storeIdx= &searchTag= &searchWord= &cursorIdx= &size=
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<GetPostsRes> getPosts(Long userIdx, @RequestParam(required = false) Long storeIdx, @RequestParam(required = false) String searchTag, @RequestParam(required = false) String searchWord, @RequestParam(required = false) Long cursorIdx, @RequestParam(required = false) Integer size){
        try{
            if(size == null) size = DEFAULT_SIZE;
            if(size < 1) return new BaseResponse<>(INVALID_POSTS_SIZE);
            if((storeIdx == null?0:1)+(searchWord == null?0:1)+(searchTag == null?0:1)>1)
                return new BaseResponse<>(MANY_PARAMETER);

            Pageable pageable = PageRequest.of(0, size);

            if (storeIdx != null) return new BaseResponse<>(this.postService.getPosts(userIdx, storeIdx, cursorIdx, pageable));
            else if (searchWord != null) return new BaseResponse<>(this.postService.getSearchPosts(userIdx,searchWord, cursorIdx, pageable));
            else if (searchTag != null) return new BaseResponse<>(this.postService.getPostsByTag(userIdx, searchTag, cursorIdx, pageable));
            else return new BaseResponse<>(NO_PARAMETER);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
