package com.codepatissier.keki.dessert.service;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.Role;
import com.codepatissier.keki.dessert.dto.*;
import com.codepatissier.keki.dessert.entity.Dessert;
import com.codepatissier.keki.dessert.repository.DessertRepository;
import com.codepatissier.keki.post.entity.PostImg;
import com.codepatissier.keki.post.repository.PostRepository;
import com.codepatissier.keki.store.entity.Store;
import com.codepatissier.keki.store.repository.StoreRepository;
import com.codepatissier.keki.user.entity.User;
import com.codepatissier.keki.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.codepatissier.keki.common.BaseResponseStatus.*;
import static com.codepatissier.keki.common.Constant.INACTIVE_STATUS;

@Service
@RequiredArgsConstructor
public class DessertService {
    private final DessertRepository dessertRepository;
    private final StoreRepository storeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 상품 전체 조회
     */
    public GetStoreDessertsRes getDessertList(Long storeIdx, Long cursorIdx, Integer size) throws BaseException {
        try {
            Store store = storeRepository.findById(storeIdx).orElseThrow(() -> new BaseException(INVALID_STORE_IDX));

            List<GetStoreDessertsRes.Dessert> dessertList = cursorIdx == null ?
                    getDessertList(store, size) :
                    getDessertListWithCursor(store, cursorIdx, size);

            Long lastIdxOfList = getLastIdxOfList(dessertList);

            return new GetStoreDessertsRes(dessertList, lastIdxOfList, hasNext(lastIdxOfList));
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private List<GetStoreDessertsRes.Dessert> getDessertList(Store store, Integer size) {
            return dessertRepository.findByStoreOrderByDessertIdxDesc(store, PageRequest.of(0, size)).stream()
                    .map(dessert -> new GetStoreDessertsRes.Dessert(dessert.getDessertIdx(),
                            dessert.getDessertImg(), dessert.getDessertName())).collect(Collectors.toList());
    }

    private List<GetStoreDessertsRes.Dessert> getDessertListWithCursor(Store store, Long cursorIdx, Integer size) throws BaseException {
        dessertRepository.findById(cursorIdx).orElseThrow(() -> new BaseException(INVALID_DESSERT_IDX));

        return dessertRepository.findByStoreAndDessertIdxLessThanOrderByDessertIdxDesc(store, cursorIdx, PageRequest.of(0, size)).stream()
                .map(dessert -> new GetStoreDessertsRes.Dessert(dessert.getDessertIdx(),
                        dessert.getDessertImg(), dessert.getDessertName())).collect(Collectors.toList());
    }

    private static Long getLastIdxOfList(List<GetStoreDessertsRes.Dessert> dessertList) {
        return dessertList.isEmpty() ? null : dessertList.get(dessertList.size() - 1).getDessertIdx();
    }

    private Boolean hasNext(Long lastIdx) {
        return lastIdx != null && this.dessertRepository.existsByDessertIdxLessThan(lastIdx);
    }

    /**
     * 상품 상세 조회
     */
    public GetDessertRes getDessert(Long dessertIdx) throws BaseException {
        try {
            Dessert dessert = dessertRepository.findById(dessertIdx).orElseThrow(() -> new BaseException(INVALID_DESSERT_IDX));

            List<GetDessertRes.Image> imgList = getPostImgList(dessert);

            // nickname, dessertName, dessertPrice, dessertDescription, imgList
            return new GetDessertRes(dessert.getStore().getUser().getNickname(), dessert.getDessertName(), dessert.getDessertPrice(), dessert.getDessertDescription(), imgList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 특정 dessert가 들어간 post 5개를 가져와 postIdx와 대표 postImg를 반환
    private List<GetDessertRes.Image> getPostImgList(Dessert dessert) {
        return postRepository.findTop5ByDessertOrderByPostIdxDesc(dessert).stream()
                .map(posts -> new GetDessertRes.Image(posts.getPostIdx(),
                        representPostImgUrl(posts.getImages()))).collect(Collectors.toList());
    }

    private String representPostImgUrl(List<PostImg> postImages){
        return postImages.isEmpty() ? null : postImages.get(0).getImgUrl();
    }

    /**
     * 상품 등록
     * 상품 이름, 가격, 소개, 이미지(1장)
     */
    public void addDessert(Long userIdx, PostDessertReq postDessertReq) throws BaseException {
        try {
            User user = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            if (!Role.getRoleByName(user.getRole()).equals(Role.STORE)) throw new BaseException(NO_STORE_ROLE);

            Store store = storeRepository.findByUser(user).orElseThrow(() -> new BaseException(INVALID_STORE_IDX));

            Dessert dessert = Dessert.builder()
                    .store(store)
                    .dessertName(postDessertReq.getDessertName())
                    .dessertPrice(postDessertReq.getDessertPrice())
                    .dessertDescription(postDessertReq.getDessertDescription())
                    .dessertImg(postDessertReq.getDessertImg())
                    .build();
            dessertRepository.save(dessert);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 상품 삭제
     */
    public void deleteDessert(Long userIdx, Long dessertIdx) throws BaseException {
        try {
            User user = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            if (!Role.getRoleByName(user.getRole()).equals(Role.STORE)) throw new BaseException(NO_STORE_ROLE);

            Dessert dessert = dessertRepository.findById(dessertIdx).orElseThrow(() -> new BaseException(INVALID_DESSERT_IDX));
            dessert.setStatus(INACTIVE_STATUS);
            dessertRepository.save(dessert);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
