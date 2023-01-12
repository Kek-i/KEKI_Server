package com.codepatissier.keki.store.controller;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.store.dto.PostStoreReq;
import com.codepatissier.keki.store.service.StoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Tag(name = "stores", description = "판매자 API")
@RestController
@RequestMapping(value = "/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    /**
     * 판매자 회원가입
     * [POST] /stores/signup
     */
    @ResponseBody
    @PostMapping("/signup")
    public BaseResponse<String> createSeller(@Valid @RequestBody PostStoreReq postStoreReq) {
        try {
            storeService.createSeller(postStoreReq);
            return new BaseResponse<>("회원 가입이 완료되었습니다!");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 판매자 사업자 정보 조회
     * GET /stores/store-info
     */


    /**
     * 판매자 마이페이지 조회
     * GET /stores/mypage
     */

    /**
     * 판매자 프로필 정보 수정
     * PATCH /stores/mypage
     */
}
