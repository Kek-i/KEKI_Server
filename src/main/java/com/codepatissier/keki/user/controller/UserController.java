package com.codepatissier.keki.user.controller;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.user.dto.*;
import com.codepatissier.keki.user.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import com.codepatissier.keki.user.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

import static com.codepatissier.keki.common.BaseResponseStatus.*;

@SecurityRequirement(name = "Bearer")
@Tag(name = "users", description = "구매자 API")
@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    // 소셜 로그인/회원가입
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<?> login(@RequestBody PostUserReq postUserReq) {
        try{
            if(postUserReq.getEmail() == null) throw new BaseException(NULL_EMAIL);
            if(postUserReq.getProvider() == null) throw new BaseException(NULL_PROVIDER);
            return new BaseResponse<>(userService.login(postUserReq.getEmail(), postUserReq.getProvider()));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 구매자 회원가입
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
            return new BaseResponse<>(SUCCESS);
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

    // 구매자 프로필 정보 수정
    @ResponseBody
    @PatchMapping("/profile")
    public BaseResponse<?> modifyProfile(@RequestBody PatchProfileReq patchProfileReq) {
        try{
            Long userIdx = authService.getUserIdx();
            userService.modifyProfile(userIdx, patchProfileReq);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 회원 탈퇴
    @DeleteMapping("/signout")
    public BaseResponse<?> signout() {
        try{
            Long userIdx = authService.getUserIdx();
            userService.signout(userIdx);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 회원 로그아웃
    @PatchMapping("/logout")
    public BaseResponse<?> logout() {
        try{
            Long userIdx = authService.getUserIdx();
            userService.logout(userIdx);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // AccessToken 재발급
    @ResponseBody
    @PostMapping("/reissue")
    public BaseResponse<?> reissueToken(@RequestBody PostTokenReq postTokenReq) {
        try{
            return new BaseResponse<>(userService.reissueToken(postTokenReq));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

}
