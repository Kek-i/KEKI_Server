package com.codepatissier.keki.user.service;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponseStatus;
import com.codepatissier.keki.common.Role;
import com.codepatissier.keki.user.dto.*;
import com.codepatissier.keki.user.entity.Provider;
import com.codepatissier.keki.user.entity.User;
import com.codepatissier.keki.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import static com.codepatissier.keki.common.BaseResponseStatus.*;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;

    // 로그인
    public PostUserRes login(String email, String provider) throws BaseException{
        try {
            if(Provider.getProviderByName(provider) == null) throw new BaseException(INVALID_PROVIDER);
            return signInOrUp(email, Provider.getProviderByName(provider));
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 회원가입 또는 기존 로그인
    private PostUserRes signInOrUp(String email, Provider provider) throws BaseException {
        boolean is_user = userRepository.existsByEmailAndProvider(email, provider);
        User user;
        if (!is_user) user = signup(email, provider);
        else user = userRepository.findByEmailAndProvider(email, provider);
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