package com.codepatissier.keki.store.controller;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.store.dto.*;
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
     * [판매자] 회원가입
     * [POST] /stores/signup
     */
    @ResponseBody
    @PostMapping("/signup")
    public BaseResponse<PostStoreRes> signup(@Valid @RequestBody PostStoreReq postStoreReq) {
        try {
            return new BaseResponse<>(storeService.signUpStore(authService.getUserIdx(), postStoreReq));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 판매자 사업자 정보 조회
     * [GET] /stores/store-info/:storeIdx
     * @return businessName, brandName, businessAddress, businessNumber
     */
    @ResponseBody
    @GetMapping("/store-info/{storeIdx}")
    public BaseResponse<GetStoreInfoRes> getStoreInfo(@PathVariable("storeIdx") Long storeIdx) {
        try {
            GetStoreInfoRes getStoreInfoRes = storeService.getStoreInfo(storeIdx);
            return new BaseResponse<>(getStoreInfoRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 판매자 프로필 조회
     * [GET] /stores/profile/:storeIdx
     * 가게 메인 화면
     * @return 가게 사진, 이름, 소개
     */
    @ResponseBody
    @GetMapping("/profile/{storeIdx}")
    public BaseResponse<GetStoreProfileRes> getStoreProfile(@PathVariable("storeIdx") Long storeIdx) {
        try {
            return new BaseResponse<>(storeService.getStoreProfile(storeIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * [판매자] 판매자 마이페이지 조회
     * [GET] /stores/mypage
     * 마이페이지-판매자
     * @return 가게 사진, 가게 이름, 이메일, '주문대기'수, '제작중'수, '픽업대기'수
     */
    @ResponseBody
    @GetMapping("/mypage")
    public BaseResponse<GetStoreMyPageRes> getStoreMyPage() {
        try {
            GetStoreMyPageRes getStoreMyPageRes = storeService.getStoreMyPage(authService.getUserIdx());
            return new BaseResponse<>(getStoreMyPageRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * [판매자] 판매자 프로필 편집 화면 조회
     * [GET] /stores/profile
     * 마이페이지 판매자 프로필 편집 화면
     * @return 가게 사진, 이름, 주소, 소개, 사업자 정보, 이메일, 가게 Idx, 예금주, 계좌번호
     */
    @ResponseBody
    @GetMapping("/profile")
    public BaseResponse<GetMyPageStoreProfileRes> getStoreProfileMyPage() {
        try {
            GetMyPageStoreProfileRes getMyPageStoreProfileRes = storeService.getStoreProfileMyPage(authService.getUserIdx());
            return new BaseResponse<>(getMyPageStoreProfileRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * [판매자] 프로필 정보 수정
     * [PATCH] /stores/profile
     */
    @ResponseBody
    @PatchMapping("/profile")
    public BaseResponse<String> patchProfile(@RequestBody PatchProfileReq patchProfileReq) {
        try {
            storeService.modifyProfile(authService.getUserIdx(), patchProfileReq);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
