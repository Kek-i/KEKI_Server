package com.codepatissier.keki.history.controller;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.history.dto.PostSearchRes;
import com.codepatissier.keki.history.dto.SearchRes;
import com.codepatissier.keki.history.entity.PostHistory;
import com.codepatissier.keki.history.service.PostHistoryService;
import com.codepatissier.keki.history.service.SearchHistoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "histories", description = "검색,조회 기록 API")
@RestController
@RequestMapping(value = "/histories")
@RequiredArgsConstructor
public class HistoryController {
    private final SearchHistoryService searchHistoryService;
    private final PostHistoryService postHistoryService;

    /**
     * token 변경후에 pathVariable 삭제 할 예정이에요!
     */
    @ResponseBody
    @GetMapping("/recent-searches/{userIdx}")
    public BaseResponse<List<SearchRes>> recentSearch(@PathVariable("userIdx")Long userIdx){
        try{
            return new BaseResponse<>(this.searchHistoryService.recentSearch(userIdx));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

    /**
     * token 변경후에 pathVariable 삭제 할 예정이에요!
     */
    @ResponseBody
    @GetMapping("/popular-recent-posts/{userIdx}")
    public BaseResponse<List<PostSearchRes>> recentSearchCake(@PathVariable("userIdx")Long userIdx){
        try{
            return new BaseResponse<>(this.postHistoryService.recentSearchCake(userIdx));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

}
