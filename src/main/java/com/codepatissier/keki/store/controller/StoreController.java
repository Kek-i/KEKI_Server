package com.codepatissier.keki.store.controller;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.store.dto.GetProfileRes;
import com.codepatissier.keki.store.dto.GetStoreInfoRes;
import com.codepatissier.keki.store.dto.PostStoreReq;
import com.codepatissier.keki.store.service.StoreService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.codepatissier.keki.user.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.codepatissier.keki.common.BaseResponseStatus.SUCCESS;

@SecurityRequirement(name = "Bearer")
@Tag(name = "stores", description = "판매자 API")
@RestController
@RequestMapping(value = "/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final AuthService authService;

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
     * @return businessName, brandName, businessAddress, businessNumber
     */
    @ResponseBody
    @GetMapping("/store-info")
    public BaseResponse<GetStoreInfoRes> getStoreInfo() {
        try {
            GetStoreInfoRes getStoreInfoRes = storeService.getStoreInfo(authService.getUserIdx());
            return new BaseResponse<>(getStoreInfoRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 판매자 프로필 조회
     * [GET] /stores/profile
     */
    @ResponseBody
    @GetMapping("/profile")
    public BaseResponse<GetProfileRes> getStoreProfile() {
        try {
            GetProfileRes getProfileRes = storeService.getStoreProfile(authService.getUserIdx());
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
