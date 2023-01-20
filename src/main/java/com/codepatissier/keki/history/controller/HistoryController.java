package com.codepatissier.keki.history.controller;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.common.BaseResponseStatus;
import com.codepatissier.keki.history.dto.PostSearchRes;
import com.codepatissier.keki.history.dto.SearchRes;
import com.codepatissier.keki.history.service.PostHistoryService;
import com.codepatissier.keki.history.service.SearchHistoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.codepatissier.keki.user.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "Bearer")
@Tag(name = "histories", description = "검색,조회 기록 API")
@RestController
@RequestMapping(value = "/histories")
@RequiredArgsConstructor
public class HistoryController {
    private final SearchHistoryService searchHistoryService;
    private final PostHistoryService postHistoryService;
    private final AuthService authService;

    // 최근 검색어
    @ResponseBody
    @GetMapping("/recent-searches")
    public BaseResponse<List<SearchRes>> recentSearch(){
        try{
            return new BaseResponse<>(this.searchHistoryService.recentSearch(this.authService.getUserIdx()));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

    // 최근 본 케이크 조회
    @ResponseBody
    @GetMapping("/recent-posts")
    public BaseResponse<List<PostSearchRes>> recentSearchCake(){
        try{
            return new BaseResponse<>(this.postHistoryService.recentSearchPost(this.authService.getUserIdx()));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 최근 검색어 삭제
    @ResponseBody
    @PatchMapping("")
    public BaseResponse<String> deleteRecentHistories(){
        try{
            this.searchHistoryService.deleteRecentHistories(this.authService.getUserIdx());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * Token 없이 사용 가능 api
     */
    // 인기 검색어
    @ResponseBody
    @GetMapping("/popular-searches")
    public BaseResponse<List<SearchRes>> getPopularSearches(){
        try{
            return new BaseResponse<>(this.searchHistoryService.getPopularSearches());
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

}
