package com.codepatissier.keki.user.service;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponseStatus;
import com.codepatissier.keki.common.Role;
import com.codepatissier.keki.user.dto.*;
import com.codepatissier.keki.user.entity.Provider;
import com.codepatissier.keki.user.entity.User;
import com.codepatissier.keki.user.repository.UserRepository;
import com.github.scribejava.core.model.OAuth2AccessToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

import static com.codepatissier.keki.common.BaseResponseStatus.*;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final KakaoService kakaoService;
    private final NaverService naverService;
    private final GoogleService googleService;

    // 카카오 로그인
    public PostUserRes kakaoLogin(String authorize_code) throws BaseException{
        try {
            String kakaoToken = kakaoService.getAccessToken(authorize_code);
            String userEmail = kakaoService.getUserInfo(kakaoToken);
            return signInOrUp(userEmail, Provider.KAKAO);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 네이버 로그인
    public PostUserRes naverLogin(String code, HttpSession session) throws BaseException{
        try{
            OAuth2AccessToken naverToken = naverService.getAccessToken(session, code);
            String userEmail = naverService.getUserInfo(naverToken);
            return signInOrUp(userEmail, Provider.NAVER);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 구글 로그인
    public PostUserRes googleLogin(String code, HttpSession session) throws BaseException{
        try {
            String googleToken = googleService.getAccessToken(session, code);
            String userEmail = googleService.getUserInfo(googleToken);
            return signInOrUp(userEmail, Provider.GOOGLE);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 회원가입 또는 기존 로그인
    private PostUserRes signInOrUp(String userEmail, Provider provider) throws BaseException {
        boolean is_user = userRepository.existsByEmail(userEmail);
        User user;
        if (!is_user) user = signup(userEmail, provider);
        else user = userRepository.findByEmail(userEmail).orElseThrow(() -> new BaseException(INVALID_EMAIL));
        return authService.createToken(user);
    }

    // ADMIN 유저 생성
    private User signup(String email, Provider provider) {
        User newUser = User.builder()
                .email(email)
                .provider(provider)
                .role(Role.ANONYMOUS)
                .build();
        return userRepository.save(newUser);
    }

    // 테스트 편리 위한 임시 로그인
    public PostUserRes signinEmail(PostUserReq postUserReq) throws BaseException{
        User user = userRepository.findByEmail(postUserReq.getEmail()).orElseThrow(() -> new BaseException(INVALID_EMAIL));
        Long userIdx = user.getUserIdx();
        String accessToken = authService.createAccessToken(userIdx);
        String refreshToken = authService.createRefreshToken(userIdx);

        return new PostUserRes(accessToken, refreshToken, user.getRole());
    }

    // 구매자 회원가입
    @Transactional
    public PostUserRes signupCustomer(Long userIdx, PostCustomerReq postCustomerReq) throws BaseException{
        try {
            User user = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            String accessToken = authService.createAccessToken(userIdx);
            String refreshToken = authService.createRefreshToken(userIdx);

            user.signup(postCustomerReq.getNickname(), refreshToken, Role.CUSTOMER, postCustomerReq.getProfileImg());
            userRepository.save(user);

            return new PostUserRes(accessToken, refreshToken, user.getRole());
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 닉네임 중복 확인
    public void checkNickname(PostNicknameReq postNicknameReq) throws BaseException{
        boolean existence = userRepository.existsByNickname(postNicknameReq.getNickname());
        if(existence) throw new BaseException(BaseResponseStatus.EXIST_NICKNAME);
    }

    // 마이페이지 조회
    public GetProfileRes getProfile() throws BaseException {
        Long userIdx = authService.getUserIdx();
        User user = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        return new GetProfileRes(user.getNickname(), user.getProfileImg());}

    // 구매자 프로필 수정
    @Transactional
    public void modifyProfile(Long userIdx, PatchProfileReq patchProfileReq) throws BaseException{
        try {
            User user = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            if (patchProfileReq.getNickname() != null) user.modifyNickname(patchProfileReq.getNickname());
            if (patchProfileReq.getProfileImg() != null) user.modifyProfileImg(patchProfileReq.getProfileImg());
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    // 회원 탈퇴
    @Transactional
    public void signout(Long userIdx) throws BaseException {
        try{
            User user = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            user.signout();
            // TODO redis 사용해 토큰 관리
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 회원 로그아웃
    @Transactional
    public void logout(Long userIdx) throws BaseException {
        try{
            User user = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            user.logout();
            // TODO redis 사용해 토큰 관리
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}