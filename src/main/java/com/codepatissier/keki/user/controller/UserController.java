package com.codepatissier.keki.user.controller;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.common.BaseResponseStatus;
import com.codepatissier.keki.user.dto.PostCustomerReq;
import com.codepatissier.keki.user.dto.PostNicknameReq;
import com.codepatissier.keki.user.dto.PostUserReq;
import com.codepatissier.keki.user.service.AuthService;
import com.codepatissier.keki.user.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Bearer")
@Tag(name = "users", description = "구매자 API")
@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    // 서버에서 모든 로직을 처리하는 경우 회원가입/로그인
    @ResponseBody
    @GetMapping("/login/kakao")
    public BaseResponse<?> kakaoCallback(@RequestParam String code) {
        try{
            return new BaseResponse<>(userService.kakaoLogin(code));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 프론트로부터 이메일만 받아오는 경우 로그인
    @ResponseBody
    @PostMapping("/signin")
    public BaseResponse<?> login(@RequestBody PostUserReq postUserReq) {
        try{
            return new BaseResponse<>(userService.signinEmail(postUserReq));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

    // 프론트로부터 이메일만 받아오는 경우 구매자 회원가입
    @ResponseBody
    @PostMapping("/signup")
    public BaseResponse<?> signup(@RequestBody PostCustomerReq postCustomerReq) {
        try{
            return new BaseResponse<>(userService.signupCustomer(authService.getUserIdx(), postCustomerReq));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

    // 닉네임 중복 확인
    @ResponseBody
    @PostMapping("/nickname")
    public BaseResponse<?> checkNickname(@RequestBody PostNicknameReq postNicknameReq) {
        try{
            userService.checkNickname(postNicknameReq);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 마이페이지 조회
    @ResponseBody
    @GetMapping("/profile")
    public BaseResponse<?> getProfile() {
        try{
            return new BaseResponse<>(userService.getProfile());
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

}
