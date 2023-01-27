package com.codepatissier.keki.user.controller;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.common.BaseResponseStatus;
import com.codepatissier.keki.user.dto.PatchProfileReq;
import com.codepatissier.keki.user.dto.PostCustomerReq;
import com.codepatissier.keki.user.dto.PostNicknameReq;
import com.codepatissier.keki.user.dto.PostUserReq;
import com.codepatissier.keki.user.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import com.codepatissier.keki.user.service.AuthService;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.net.URISyntaxException;

import static com.codepatissier.keki.common.BaseResponseStatus.SUCCESS;

@SecurityRequirement(name = "Bearer")
@Tag(name = "users", description = "구매자 API")
@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final NaverService naverService;
    private final KakaoService kakaoService;
    private final GoogleService googleService;

    // 카카오 로그인 url 요청
    @GetMapping("/login/kakao")
    public BaseResponse<?> kakaoLogin(HttpSession session) {
        String httpHeaders = kakaoService.getAuthorizationUrl(session);
        return new BaseResponse<>(httpHeaders);
    }

    // 카카오 로그인 콜백
    @ResponseBody
    @GetMapping("/callback/kakao")
    public BaseResponse<?> kakaoCallback(@RequestParam String code) {
        try{
            return new BaseResponse<>(userService.kakaoLogin(code));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 네이버 로그인 url 요청
    @GetMapping("/login/naver")
    public BaseResponse<?> login(HttpSession session) {
        String httpHeaders = naverService.getAuthorizationUrl(session);
        return new BaseResponse<>(httpHeaders);
    }

    // 네이버 로그인 콜백
    @GetMapping("/callback/naver")
    public BaseResponse<?> naverCallback(@RequestParam String code, HttpSession session) {
        try{
            return new BaseResponse<>(userService.naverLogin(code, session));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 구글 로그인 url 요청
    @GetMapping("/login/google")
    public BaseResponse<?> googleLogin(HttpSession session) {
        String httpHeaders = googleService.getAuthorizationUrl(session);
        return new BaseResponse<>(httpHeaders);
    }

    // 구글 로그인 콜백
    @GetMapping("/callback/google")
    public BaseResponse<?> googleCallback(@RequestParam String code, HttpSession session) {
        try{
            return new BaseResponse<>(userService.googleLogin(code, session));
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
    @PatchMapping("/signout")
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

}
