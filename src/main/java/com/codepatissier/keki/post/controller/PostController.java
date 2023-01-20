package com.codepatissier.keki.post.controller;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.cs.entity.ReportCategory;
import com.codepatissier.keki.post.dto.GetStorePostsRes;
import com.codepatissier.keki.post.dto.PostReportReq;
import com.codepatissier.keki.post.service.PostService;
import com.codepatissier.keki.user.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import static com.codepatissier.keki.common.BaseResponseStatus.*;
import static com.codepatissier.keki.common.Constant.Posts.*;

@Tag(name = "posts", description = "피드 API")
@RestController
@RequestMapping(value = "/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final AuthService authService;

    /**
     * 스토어별 피드 목록 조회(최초 호출)
     * [GET] /posts? storeIdx= &size=
     * or
     * 스토어별 피드 목록 조회(다음 호출)
     * [GET] /posts? storeIdx= &cursorIdx= &size=
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetStorePostsRes> getPostList(Long storeIdx, @RequestParam(required = false) Long cursorIdx, @RequestParam(required = false) Integer size){
        try{
            if(size == null) size = DEFAULT_SIZE;
            if(size < 1) return new BaseResponse<>(INVALID_POSTS_SIZE);

            return new BaseResponse<>(this.postService.getPostList(storeIdx, cursorIdx, PageRequest.of(0, size)));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 게시물 신고
     * [POST] /posts/:postIdx/report
     */
    @ResponseBody
    @PostMapping("/{postIdx}/report")
    public BaseResponse<String> doReport(@RequestBody PostReportReq postReportReq, @PathVariable Long postIdx){
        try{
            if (ReportCategory.getReportCategoryByName(postReportReq.getReportName()) == null)
                return new BaseResponse<>(INVALID_REPORT_CATEGORY);
            this.postService.doReport(authService.getUserIdx(), postReportReq, postIdx);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 게시물 좋아요/취소
     * [POST] /posts/:postIdx/like
     */
    @ResponseBody
    @PostMapping("/{postIdx}/like")
    public BaseResponse<String> doLike(@PathVariable Long postIdx){
        try{
            this.postService.doLike(authService.getUserIdx(), postIdx);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
