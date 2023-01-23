package com.codepatissier.keki.store.service;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.store.dto.GetMyPageStoreProfileRes;
import com.codepatissier.keki.store.dto.GetStoreInfoRes;
import com.codepatissier.keki.store.dto.GetStoreProfileRes;
import com.codepatissier.keki.store.dto.PostStoreReq;
import com.codepatissier.keki.store.entity.Store;
import com.codepatissier.keki.store.repository.StoreRepository;
import com.codepatissier.keki.user.entity.User;
import com.codepatissier.keki.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.codepatissier.keki.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    // 회원가입 (프로필 정보 post)
    public void createSeller(Long userIdx, PostStoreReq postStoreReq) throws BaseException {
        try {
            User user = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            user.storeSignUp(postStoreReq.getNickname(), postStoreReq.getStoreImgUrl());
            userRepository.save(user);

            Store store = Store.builder()
                    .user(user)
                    .address(postStoreReq.getAddress())
                    .introduction(postStoreReq.getIntroduction())
                    .orderUrl(postStoreReq.getOrderUrl())
                    .businessName(postStoreReq.getBusinessName())
                    .brandName(postStoreReq.getBrandName())
                    .businessAddress(postStoreReq.getBusinessAddress())
                    .businessNumber(postStoreReq.getBusinessNumber())
                    .build();
            storeRepository.save(store);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 사업자 정보 조회
    public GetStoreInfoRes getStoreInfo(Long storeIdx) throws BaseException {
        try {
            Store store = storeRepository.findById(storeIdx).orElseThrow(() -> new BaseException(INVALID_STORE_IDX));

            return new GetStoreInfoRes(store.getBusinessName(), store.getBrandName(), store.getBusinessAddress(), store.getBusinessNumber());
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 판매자 프로필 조회
    // 가게 사진, 이름, 소개
    public GetStoreProfileRes getStoreProfile(Long storeIdx) throws BaseException {
        try {
            Store store = storeRepository.findById(storeIdx).orElseThrow(() -> new BaseException(INVALID_STORE_IDX));

            return new GetStoreProfileRes(store.getUser().getNickname(), store.getUser().getProfileImg(), store.getIntroduction());
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 마이페이지 판매자 프로필 조회
    // 가게 사진, 이름, 주소, 소개, 주문 링크
    public GetMyPageStoreProfileRes getStoreProfileMyPage(Long userIdx) throws BaseException {
        try {
            User user = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            Store store = storeRepository.findByUser(user);
            if (store == null) throw new BaseException(INVALID_STORE_IDX);

            return new GetMyPageStoreProfileRes(store.getUser().getProfileImg(), store.getUser().getNickname(), store.getAddress(), store.getIntroduction(), store.getOrderUrl());
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
