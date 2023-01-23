package com.codepatissier.keki.cs.controller;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.cs.service.CsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Bearer")
@Tag(name = "cs", description = "CS API")
@RestController
@RequestMapping(value = "/cs")
@RequiredArgsConstructor
public class CsController {
    private final CsService csService;

    // 공지사항 전체 조회
    @GetMapping("/notice")
    public BaseResponse<?> getNoticeList() {
        try{
            return new BaseResponse<>(csService.getNoticeList());
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 공지사항 상세 조회
    @GetMapping("/notice/{noticeIdx}")
    public BaseResponse<?> getNotice(@PathVariable("noticeIdx") Long noticeIdx) {
        try{
            return new BaseResponse<>(csService.getNotice(noticeIdx));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


}
