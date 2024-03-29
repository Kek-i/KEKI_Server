package com.codepatissier.keki.post.controller;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.post.dto.*;
import com.codepatissier.keki.cs.entity.ReportCategory;
import com.codepatissier.keki.post.service.PostService;
import com.codepatissier.keki.user.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.codepatissier.keki.common.BaseResponseStatus.*;
import static com.codepatissier.keki.common.Constant.Posts.*;

@SecurityRequirement(name = "Bearer")
@Tag(name = "posts", description = "피드 API")
@RestController
@RequestMapping(value = "/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final AuthService authService;

    /**
     * 피드 목록 조회(최초 호출)
     * [GET] /posts? storeIdx= &searchTag= &searchWord= &sortType= &size=
     * or
     * 피드 목록 조회(다음 호출)
     * [GET] /posts? storeIdx= &searchTag= &searchWord= &sortType= &cursorIdx= &cursorPrice= &cursorPopularNum &size=
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetPostsRes> getPosts(@RequestParam(required = false) Long storeIdx,
                                              @RequestParam(required = false) String searchTag,
                                              @RequestParam(required = false) String searchWord,
                                              @RequestParam(required = false) String sortType,
                                              @RequestParam(required = false) Long cursorIdx,
                                              @RequestParam(required = false) Integer cursorPrice,
                                              @RequestParam(required = false) Long cursorPopularNum,
                                              @RequestParam(required = false) Integer size){
        try{
            if(size == null) size = DEFAULT_SIZE;
            if(size < 1) return new BaseResponse<>(INVALID_POSTS_SIZE);
            if((storeIdx == null ? 0 : 1) + (searchWord == null ? 0 : 1) + (searchTag == null ? 0 : 1) > 1)
                return new BaseResponse<>(MANY_PARAMETER);
            if(sortType != null && storeIdx != null) return new BaseResponse<>(DO_NOT_STORE_SORT_TYPE);
            if(sortType == null) sortType = NEW_SORT_TYPE;
            if(cursorIdx != null && !sortType.equals(NEW_SORT_TYPE)){
                if (cursorPrice == null && cursorPopularNum == null) return new BaseResponse<>(NULL_CURSOR);
                if ((sortType.equals(LOW_PRICE_SORT_TYPE) && cursorPrice == null) ||
                        (sortType.equals(POPULAR_SORT_TYPE) && cursorPopularNum == null))
                    return new BaseResponse<>(INVALID_SORT_TYPE_CURSOR);
                if(cursorPrice != null && cursorPopularNum != null) return new BaseResponse<>(MANY_CURSOR_PARAMETER);
            }
            if(cursorIdx == null && (cursorPrice != null || cursorPopularNum != null))
                return new BaseResponse<>(NULL_CURSOR);

            Pageable pageable = PageRequest.of(0, size);

            if (storeIdx != null) return new BaseResponse<>(this.postService.getPosts(authService.getUserIdxOptional(), storeIdx, cursorIdx, pageable));
            else if (searchWord != null) return new BaseResponse<>(this.postService.getSearchPosts(authService.getUserIdxOptional(),searchWord, sortType, cursorIdx, cursorPrice, cursorPopularNum, pageable));
            else if (searchTag != null) return new BaseResponse<>(this.postService.getPostsByTag(authService.getUserIdxOptional(), searchTag, sortType, cursorIdx, cursorPrice, cursorPopularNum, pageable));
            else return new BaseResponse<>(NO_PARAMETER);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 피드 개별 조회
     * [GET] /posts/:postIdx
     */
    @ResponseBody
    @GetMapping("/{postIdx}")
    public BaseResponse<GetPostRes> getPost(@PathVariable Long postIdx) {
        try {
            //TODO: blank 처리
            if(postIdx == null) return  new BaseResponse<>(NULL_POST_IDX);
            return new BaseResponse<>(this.postService.getPost(postIdx, authService.getUserIdxOptional()));
        } catch (BaseException e){
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

    /**
     * 좋아요 피드 목록 조회
     * [GET] /posts/likes? cursorDate= &size=
     */
    @ResponseBody
    @GetMapping("/likes")
    public BaseResponse<GetLikePostsRes> getLikePosts(String cursorDate, Integer size) {
        try {
            if(size == null) size = DEFAULT_SIZE;
            if(size < 1) return new BaseResponse<>(INVALID_POSTS_SIZE);
            Pageable pageable = PageRequest.of(0, size);

            LocalDateTime date = null;
            if (cursorDate != null) date = LocalDateTime.parse(cursorDate);

            return new BaseResponse<>(this.postService.getLikePosts(authService.getUserIdx(), date, pageable));
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 게시물 조회 기록
     * [POST] /posts/:postIdx/history
     */
    @ResponseBody
    @PostMapping("/{postIdx}/history")
    public BaseResponse<String> makePostHistory(@PathVariable Long postIdx){
        try{
            this.postService.makePostHistory(authService.getUserIdx(), postIdx);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 게시물 등록 화면
     * [GET] /posts/makeView
     */
    @ResponseBody
    @GetMapping("/makeView")
    public BaseResponse<GetMakePostRes> getMakePostView(){
        try{
            return new BaseResponse<>(this.postService.getMakePostView(authService.getUserIdx()));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 게시물 등록
     * [POST] /posts
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> makePost(@RequestBody PostPostReq postPostReq){
        try{
            if (postPostReq.getPostImgUrls().size() > 5 || postPostReq.getPostImgUrls().size() < 1)
                return new BaseResponse<>(INVALID_IMAGE_NUM);
            if (postPostReq.getTags().size() > 3 || postPostReq.getTags().size() < 1)
                return new BaseResponse<>(INVALID_TAG_NUM);
            this.postService.makePost(authService.getUserIdx(), postPostReq);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 게시물 수정 화면
     * [GET] /posts/:postIdx/editView
     */
    @ResponseBody
    @GetMapping("/{postIdx}/editView")
    public BaseResponse<GetModifyPostRes> getModifyPostView(@PathVariable Long postIdx){
        try{
            return new BaseResponse<>(this.postService.getModifyPostView(authService.getUserIdx(), postIdx));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 게시물 수정
     * [PATCH] /posts/:postIdx
     */
    @ResponseBody
    @PatchMapping("/{postIdx}")
    public BaseResponse<String> modifyPost(@RequestBody PatchPostReq patchPostReq, @PathVariable Long postIdx){
        try{
            if (patchPostReq.getPostImgUrls() != null && (patchPostReq.getPostImgUrls().size() > 5 || patchPostReq.getPostImgUrls().size() < 1))
                return new BaseResponse<>(INVALID_IMAGE_NUM);
            if (patchPostReq.getTags() != null && (patchPostReq.getTags().size() > 3 || patchPostReq.getTags().size() < 1))
                return new BaseResponse<>(INVALID_TAG_NUM);
            this.postService.modifyPost(authService.getUserIdx(), postIdx, patchPostReq);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 게시물 삭제
     * [DELETE] /posts/:postIdx
     */
    @ResponseBody
    @DeleteMapping("/{postIdx}")
    public BaseResponse<String> deletePost(@PathVariable Long postIdx){
        try{
            this.postService.deletePost(authService.getUserIdx(), postIdx);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
