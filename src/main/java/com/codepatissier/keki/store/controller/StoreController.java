package com.codepatissier.keki.store.controller;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.store.dto.GetProfileRes;
import com.codepatissier.keki.store.dto.GetStoreInfoRes;
import com.codepatissier.keki.store.dto.PostStoreReq;
import com.codepatissier.keki.store.service.StoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.codepatissier.keki.common.BaseResponseStatus.SUCCESS;


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
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 판매자 사업자 정보 조회
     * [GET] /stores/store-info
     * return businessName, brandName, businessAddress, businessNumber
     */
    // Token 변경 후 Path Variable 삭제
    @ResponseBody
    @GetMapping("/store-info/{storeIdx}")
    public BaseResponse<List<GetStoreInfoRes>> getStoreInfo(@PathVariable("storeIdx") Long storeIdx) {
        try {
            List<GetStoreInfoRes> getStoreInfoRes = storeService.getStoreInfo(storeIdx);
            return new BaseResponse<>(getStoreInfoRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 판매자 프로필 조회
     * [GET] /stores/mypage
     */
    @ResponseBody
    @GetMapping("/mypage/{storeIdx}")
    public BaseResponse<List<GetProfileRes>> getMyPage(@PathVariable("storeIdx") Long storeIdx) {
        try {
            List<GetProfileRes> getProfileRes = storeService.getProfile(storeIdx);
            return new BaseResponse<>(getProfileRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 판매자 프로필 정보 수정
     * PATCH /stores/mypage
     */
}
