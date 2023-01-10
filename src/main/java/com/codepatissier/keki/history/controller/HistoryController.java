package com.codepatissier.keki.history.controller;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.common.BaseResponseStatus;
import com.codepatissier.keki.history.dto.RecentSearchRes;
import com.codepatissier.keki.history.entity.SearchHistory;
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

    /**
     * token 변경후에 pathVariable 삭제 할 예정이에요!
     */
    @ResponseBody
    @GetMapping("/recent-searches/{userIdx}")
    public BaseResponse<List<RecentSearchRes>> recentSearch(@PathVariable("userIdx")Long userIdx){
        try{
            return new BaseResponse<>(this.searchHistoryService.recentSearch(userIdx));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

}
