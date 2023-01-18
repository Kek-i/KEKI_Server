package com.codepatissier.keki.user.controller;

import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.user.dto.PostCustomerReq;
import com.codepatissier.keki.user.dto.PostUserReq;
import com.codepatissier.keki.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "users", description = "구매자 API")
@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 서버에서 모든 로직을 처리하는 경우 회원가입/로그인
    @ResponseBody
    @GetMapping("/login/kakao")
    public BaseResponse<?> kakaoCallback(@RequestParam String code) {
        return new BaseResponse<>(userService.kakaoLogin(code));
    }

    // 프론트로부터 이메일만 받아오는 경우 로그인
    @ResponseBody
    @PostMapping("/signin")
    public BaseResponse<?> login(@RequestBody PostUserReq postUserReq) {
        return new BaseResponse<>(userService.signinEmail(postUserReq));
    }

    // 프론트로부터 이메일만 받아오는 경우 구매자 회원가입
    @ResponseBody
    @PostMapping("/signup")
    public BaseResponse<?> signup(@RequestBody PostCustomerReq postCustomerReq) {
        return new BaseResponse<>(userService.signupEmail(postCustomerReq));
    }

}
