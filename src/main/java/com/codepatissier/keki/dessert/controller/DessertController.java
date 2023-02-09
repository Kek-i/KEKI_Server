package com.codepatissier.keki.dessert.controller;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.dessert.dto.*;
import com.codepatissier.keki.dessert.service.DessertService;
import com.codepatissier.keki.user.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.codepatissier.keki.common.BaseResponseStatus.INVALID_POSTS_SIZE;
import static com.codepatissier.keki.common.BaseResponseStatus.SUCCESS;
import static com.codepatissier.keki.common.Constant.Posts.DEFAULT_SIZE;

@SecurityRequirement(name = "Bearer")
@Tag(name = "desserts", description = "상품 API")
@RestController
@RequestMapping(value = "/desserts")
@RequiredArgsConstructor
public class DessertController {
    private final DessertService dessertService;
    private final AuthService authService;

    /**
     * 스토어별 상품 전체 조회
     * 최초 호출
     * [GET] /desserts?storeIdx=&size=
     *
     * 최초 아닌 호출
     * [GET] /desserts?storeIdx=&cursorIdx=&size=
     * @RequestParam cursorIdx = 현재까지 페이지에 그려진 dessert id 중 가장 작은 값
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetStoreDessertsRes> getDessertList(Long storeIdx, @RequestParam(required = false) Long cursorIdx, @RequestParam(required = false) Integer size) {
        try {
            if(size == null) size = DEFAULT_SIZE;
            if(size < 1) return new BaseResponse<>(INVALID_POSTS_SIZE);

            return new BaseResponse<>(dessertService.getDessertList(storeIdx, cursorIdx, size));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    /**
     * 상품 상세 조회
     * [GET] /desserts/:dessertIdx
     */
    @ResponseBody
    @GetMapping("/{dessertIdx}")
    public BaseResponse<GetDessertRes> getDessert(@PathVariable("dessertIdx") Long dessertIdx) {
        try {
            return new BaseResponse<>(dessertService.getDessert(dessertIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * [판매자] 상품 등록
     * [POST] /desserts
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> addDessert(@Valid @RequestBody PostDessertReq postDessertReq) {
        try {
            dessertService.addDessert(authService.getUserIdx(), postDessertReq);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * [판매자] 상품 삭제
     * [PATCH] /desserts/:dessertIdx
     */
    @ResponseBody
    @DeleteMapping("/{dessertIdx}")
    public BaseResponse<String> deleteDessert(@PathVariable("dessertIdx") Long dessertIdx) {
        try {
            dessertService.deleteDessert(authService.getUserIdx(), dessertIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * [판매자] 상품 수정
     * [PATCH] /desserts/:dessertIdx/editDessert
     */
    @ResponseBody
    @PatchMapping("/{dessertIdx}/editDessert")
    public BaseResponse<String> editDessert(@Valid @RequestBody PatchDessertReq patchDessertReq, @PathVariable("dessertIdx") Long dessertIdx) {
        try {
            dessertService.modifyDessert(patchDessertReq, dessertIdx, authService.getUserIdx());
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * [판매자] 상품 상세 조회
     * [GET] /desserts/:dessertIdx/editDessert
     */
    @ResponseBody
    @GetMapping("/{dessertIdx}/editDessert")
    public BaseResponse<GetStoreDessertRes> getStoreDessert(@PathVariable("dessertIdx") Long dessertIdx) {
        try {
          return new BaseResponse<>(dessertService.getStoreDessert(authService.getUserIdx(), dessertIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}