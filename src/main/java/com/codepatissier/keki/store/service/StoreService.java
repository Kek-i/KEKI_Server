package com.codepatissier.keki.store.service;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.Constant;
import com.codepatissier.keki.common.Role;
import com.codepatissier.keki.order.dto.NumOfOrder;
import com.codepatissier.keki.order.service.OrderService;
import com.codepatissier.keki.store.dto.*;
import com.codepatissier.keki.store.entity.Store;
import com.codepatissier.keki.store.repository.StoreRepository;
import com.codepatissier.keki.user.entity.User;
import com.codepatissier.keki.user.repository.UserRepository;
import com.codepatissier.keki.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.codepatissier.keki.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final OrderService orderService;

    // 판매자 회원가입
    // user Role=ANONYMOUS로 만들어진 상태
    @Transactional(rollbackFor = Exception.class)
    public PostStoreRes signUpStore(Long userIdx, PostStoreReq postStoreReq) throws BaseException {
        try {
            String accessToken = authService.createAccessToken(userIdx);
            String refreshToken = authService.createRefreshToken(userIdx);

            User user = userRepository.findByUserIdxAndStatusEquals(userIdx, Constant.ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            user.storeSignUp(postStoreReq.getNickname(), postStoreReq.getStoreImgUrl(), Role.STORE);
            userRepository.save(user);

            Store store = Store.builder()
                    .user(user)
                    .address(postStoreReq.getAddress())
                    .introduction(postStoreReq.getIntroduction())
                    .accountHolder(postStoreReq.getAccountHolder())
                    .accountNumber(postStoreReq.getAccountNumber())
                    .businessName(postStoreReq.getBusinessName())
                    .brandName(postStoreReq.getBrandName())
                    .businessAddress(postStoreReq.getBusinessAddress())
                    .businessNumber(postStoreReq.getBusinessNumber())
                    .build();
            storeRepository.save(store);

            return new PostStoreRes(accessToken, refreshToken, user.getRole());
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 사업자 정보 조회
    public GetStoreInfoRes getStoreInfo(Long storeIdx) throws BaseException {
        try {
            Store store = storeRepository.findByStoreIdxAndStatus(storeIdx, Constant.ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_STORE_IDX));

            return new GetStoreInfoRes(store.getBusinessName(), store.getBrandName(), store.getBusinessAddress(), store.getBusinessNumber());
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 판매자 프로필 조회
    // 가게 사진, 이름, 소개, 주문 링크
    public GetStoreProfileRes getStoreProfile(Long storeIdx) throws BaseException {
        try {
            Store store = storeRepository.findByStoreIdxAndStatus(storeIdx, Constant.ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_STORE_IDX));

            return new GetStoreProfileRes(store.getUser().getNickname(), store.getUser().getProfileImg(), store.getIntroduction());
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [판매자] 판매자 마이페이지 조회
    // 가게 사진, 가게 이름, 이메일, 주문상태별 주문수
    public GetStoreMyPageRes getStoreMyPage(Long userIdx) throws BaseException {
        try {
            User user = userRepository.findByUserIdxAndStatusEquals(userIdx, Constant.ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            storeRepository.findByUserAndStatus(user, Constant.ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_STORE_IDX));

            NumOfOrder numOfOrder = orderService.getCountByOrderStatus(user);
            return new GetStoreMyPageRes(user, numOfOrder);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [판매자] 판매자 프로필 편집 화면 조회
    // 가게 사진, 이름, 주소, 소개, 사업자 정보, 이메일, 가게 Idx, 예금주, 계좌번호
    public GetMyPageStoreProfileRes getStoreProfileMyPage(Long userIdx) throws BaseException {
        try {
            User user = userRepository.findByUserIdxAndStatusEquals(userIdx, Constant.ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            Store store = storeRepository.findByUserAndStatus(user, Constant.ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_STORE_IDX));

            return new GetMyPageStoreProfileRes(store.getStoreIdx(), store.getUser().getProfileImg(), store.getUser().getEmail(), store.getUser().getNickname(), store.getAddress(), store.getIntroduction(),
                    store.getAccountHolder(), store.getAccountNumber(), store.getBusinessName(), store.getBrandName(), store.getBusinessAddress(), store.getBusinessNumber());
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [판매자] 가게 프로필 수정
    // 가게 사진, 이름, 주소, 소개, 주문 링크, 사업자 정보
    @Transactional(rollbackFor = Exception.class)
    public void modifyProfile(Long userIdx, PatchProfileReq patchProfileReq) throws BaseException {
        try {
            User user = userRepository.findByUserIdxAndStatusEquals(userIdx, Constant.ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            Store store = storeRepository.findByUserAndStatus(user, Constant.ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_STORE_IDX));

            if (patchProfileReq.getStoreImgUrl() != null) {
                user.setProfileImg(patchProfileReq.getStoreImgUrl());
                store.setUser(user);
            }

            if (patchProfileReq.getNickname() != null) {
                if (!patchProfileReq.getNickname().equals("") && !patchProfileReq.getNickname().equals(" ")) {
                    user.setNickname(patchProfileReq.getNickname());
                    store.setUser(user);
                } else throw new BaseException(NULL_NICKNAME);
            }

            if (patchProfileReq.getAddress() != null) {
                if (!patchProfileReq.getAddress().equals("") && !patchProfileReq.getAddress().equals(" "))
                    store.setAddress(patchProfileReq.getAddress());
                else throw new BaseException(NULL_ADDRESS);
            }

            if (patchProfileReq.getIntroduction() != null) store.setIntroduction(patchProfileReq.getIntroduction());

            if (patchProfileReq.getAccountHolder() != null) {
                if (!patchProfileReq.getAccountHolder().equals("") && !patchProfileReq.getAccountHolder().equals(" "))
                    store.setAccountHolder(patchProfileReq.getAccountHolder());
                else throw new BaseException(NULL_ACCOUNT_HOLDER);
            }

            if (patchProfileReq.getAccountNumber() != null) {
                if (!patchProfileReq.getAccountNumber().equals("") && !patchProfileReq.getAccountNumber().equals(" "))
                    store.setAccountNumber(patchProfileReq.getAccountNumber());
                else throw new BaseException(NULL_ACCOUNT_NUMBER);
            }

            if (patchProfileReq.getBusinessName() != null) {
                if (!patchProfileReq.getBusinessName().equals("") && !patchProfileReq.getBusinessName().equals(" "))
                    store.setBusinessName(patchProfileReq.getBusinessName());
                else throw new BaseException(NULL_BUSINESS_NAME);
            }

            if (patchProfileReq.getBrandName() != null) {
                if (!patchProfileReq.getBrandName().equals("") && !patchProfileReq.getBrandName().equals(" "))
                    store.setBrandName(patchProfileReq.getBrandName());
                else throw new BaseException(NULL_BRAND_NAME);
            }

            if (patchProfileReq.getBusinessAddress() != null) {
                if (!patchProfileReq.getBusinessAddress().equals("") && !patchProfileReq.getBusinessAddress().equals(" "))
                    store.setBusinessAddress(patchProfileReq.getBusinessAddress());
                else throw new BaseException(NULL_BUSINESS_ADDRESS);
            }

            if (patchProfileReq.getBusinessNumber() != null) {
                if (!patchProfileReq.getBusinessNumber().equals("") && !patchProfileReq.getBusinessNumber().equals(" "))
                    store.setBusinessNumber(patchProfileReq.getBusinessNumber());
                else throw new BaseException(NULL_BUSINESS_NUMBER);
            }
            userRepository.save(user);
            storeRepository.save(store);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}