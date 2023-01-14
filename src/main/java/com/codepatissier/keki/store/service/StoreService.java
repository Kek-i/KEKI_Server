package com.codepatissier.keki.store.service;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.store.dto.GetStoreInfoRes;
import com.codepatissier.keki.store.dto.PostStoreReq;
import com.codepatissier.keki.store.entity.Store;
import com.codepatissier.keki.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<GetStoreInfoRes> getStoreInfo(Long storeIdx) throws BaseException {
        try {
            List<Store> store = (List<Store>) storeRepository.findById(storeIdx)
                    .orElseThrow(() -> new BaseException(INVALID_STORE_IDX));
            return store.stream()
                    .map(storeInfo -> new GetStoreInfoRes(storeInfo.getBusinessName(), storeInfo.getBrandName(), storeInfo.getBusinessAddress(), storeInfo.getBusinessNumber()))
                    .collect(Collectors.toList());
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
