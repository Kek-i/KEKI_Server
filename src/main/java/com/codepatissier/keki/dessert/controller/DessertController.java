package com.codepatissier.keki.dessert.controller;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.dessert.dto.GetDessertRes;
import com.codepatissier.keki.dessert.dto.GetStoreDessertsRes;
import com.codepatissier.keki.dessert.service.DessertService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.codepatissier.keki.common.BaseResponseStatus.INVALID_POSTS_SIZE;
import static com.codepatissier.keki.common.Constant.Posts.DEFAULT_SIZE;

@Tag(name = "desserts", description = "상품 API")
@RestController
@RequestMapping(value = "/desserts")
@RequiredArgsConstructor
public class DessertController {
    private final DessertService dessertService;

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
     * 상품 등록
     * [POST] /desserts
     */

    /**
     * 상품 삭제
     * [PATCH] /desserts/:dessertIdx
     */
}
