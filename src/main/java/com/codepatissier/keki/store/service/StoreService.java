package com.codepatissier.keki.store.service;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.store.dto.GetProfileRes;
import com.codepatissier.keki.store.dto.GetStoreInfoRes;
import com.codepatissier.keki.store.dto.PostStoreReq;
import com.codepatissier.keki.store.entity.Store;
import com.codepatissier.keki.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.codepatissier.keki.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    // 회원가입 (프로필 정보 post)
    public void createSeller(PostStoreReq postStoreReq) throws BaseException {
        try {
            Store store = Store.builder() // 전달 받은 postStoreReq의 정보를 Entity화
                    .address(postStoreReq.getAddress())
                    .introduction(postStoreReq.getIntroduction())
                    .orderUrl(postStoreReq.getOrderUrl())
                    .businessName(postStoreReq.getBusinessName())
                    .brandName(postStoreReq.getBrandName())
                    .businessAddress(postStoreReq.getBusinessAddress())
                    .businessNumber(postStoreReq.getBusinessNumber())
                    .build();
            this.storeRepository.save(store);
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

    // 가게 프로필 조회 (가게 사진, 이름, 소개)
    public GetProfileRes getStoreProfile(Long storeIdx) throws BaseException {
        try {
            Store store = storeRepository.findById(storeIdx).orElseThrow(() -> new BaseException(INVALID_STORE_IDX));

            return new GetProfileRes(store.getUser().getNickname(), store.getUser().getProfileImg(), store.getIntroduction());
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
