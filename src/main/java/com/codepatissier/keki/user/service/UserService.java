package com.codepatissier.keki.user.service;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.common.BaseResponseStatus;
import com.codepatissier.keki.common.Role;
import com.codepatissier.keki.user.dto.PostCustomerReq;
import com.codepatissier.keki.user.dto.PostNicknameReq;
import com.codepatissier.keki.user.dto.PostUserReq;
import com.codepatissier.keki.user.dto.PostUserRes;
import com.codepatissier.keki.user.entity.Provider;
import com.codepatissier.keki.user.entity.User;
import com.codepatissier.keki.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.codepatissier.keki.common.BaseResponseStatus.*;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final KakaoService kakaoService;

    public PostUserRes kakaoLogin(String authorize_code) throws BaseException{
        String kakaoToken = kakaoService.getAccessToken(authorize_code);
        String userEmail = kakaoService.getUserInfo(kakaoToken);

        boolean is_user = userRepository.existsByEmail(userEmail);

        if(!is_user) {
            User user = signup(userEmail, Provider.KAKAO);
            return authService.createToken(user);
        }
        else {
            User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new BaseException(INVALID_EMAIL));
            return authService.createToken(user);
        }
    }

    public PostUserRes signinEmail(PostUserReq postUserReq) throws BaseException{
        User user = userRepository.findByEmail(postUserReq.getEmail()).orElseThrow(() -> new BaseException(INVALID_EMAIL));
        Long userIdx = user.getUserIdx();
        String accessToken = authService.createAccessToken(userIdx);
        String refreshToken = authService.createRefreshToken(userIdx);

        return new PostUserRes(accessToken, refreshToken, user.getRole());
    }

    public PostUserRes signupEmail(PostCustomerReq postCustomerReq) throws BaseException{
        Long userIdx = authService.getUserIdx();
        User user = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        String accessToken = authService.createAccessToken(userIdx);
        String refreshToken = authService.createRefreshToken(userIdx);

        user.signup(postCustomerReq.getNickname(), refreshToken, Role.CUSTOMER, postCustomerReq.getProfileImg());
        userRepository.save(user);

        return new PostUserRes(accessToken, refreshToken, user.getRole());
    }

    private User signup(String email, Provider provider) {
        User newUser = User.builder()
                .email(email)
                .provider(provider)
                .role(Role.ADMIN)
                .build();
        return userRepository.save(newUser);
    }

    public void checkNickname(PostNicknameReq postNicknameReq) throws BaseException{
        boolean existence = userRepository.existsByNickname(postNicknameReq.getNickname());
        if(existence) throw new BaseException(BaseResponseStatus.EXIST_NICKNAME);
    }
}