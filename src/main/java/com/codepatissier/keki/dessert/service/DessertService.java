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
import static com.codepatissier.keki.common.Constant.ACTIVE_STATUS;
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
            Store store = storeRepository.findByStoreIdxAndStatus(storeIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_STORE_IDX));

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
            return dessertRepository.findByStoreAndStatusOrderByDessertIdxDesc(store, ACTIVE_STATUS, PageRequest.of(0, size)).stream()
                    .map(dessert -> new GetStoreDessertsRes.Dessert(dessert.getDessertIdx(),
                            dessert.getDessertImg(), dessert.getDessertName())).collect(Collectors.toList());
    }

    private List<GetStoreDessertsRes.Dessert> getDessertListWithCursor(Store store, Long cursorIdx, Integer size) throws BaseException {
        dessertRepository.findByDessertIdxAndStatus(cursorIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_DESSERT_IDX));

        return dessertRepository.findByStoreAndStatusAndDessertIdxLessThanOrderByDessertIdxDesc(store, ACTIVE_STATUS, cursorIdx, PageRequest.of(0, size)).stream()
                .map(dessert -> new GetStoreDessertsRes.Dessert(dessert.getDessertIdx(),
                        dessert.getDessertImg(), dessert.getDessertName())).collect(Collectors.toList());
    }

    private static Long getLastIdxOfList(List<GetStoreDessertsRes.Dessert> dessertList) {
        return dessertList.isEmpty() ? null : dessertList.get(dessertList.size() - 1).getDessertIdx();
    }

    private Boolean hasNext(Long lastIdx) {
        return lastIdx != null && this.dessertRepository.existsByStatusAndDessertIdxLessThan(ACTIVE_STATUS, lastIdx);
    }

    /**
     * 상품 상세 조회
     */
    public GetDessertRes getDessert(Long dessertIdx) throws BaseException {
        try {
            Dessert dessert = dessertRepository.findByDessertIdxAndStatus(dessertIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_DESSERT_IDX));

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
        return postRepository.findTop5ByDessertAndStatusOrderByPostIdxDesc(dessert, ACTIVE_STATUS).stream()
                .map(posts -> new GetDessertRes.Image(posts.getPostIdx(),
                        representPostImgUrl(posts.getImages()))).collect(Collectors.toList());
    }

    private String representPostImgUrl(List<PostImg> postImages){
        return postImages.isEmpty() ? null : postImages.get(0).getImgUrl();
    }

    /**
     * [판매자] 상품 등록
     * 상품 이름, 가격, 소개, 이미지(1장)
     */
    public void addDessert(Long userIdx, PostDessertReq postDessertReq) throws BaseException {
        try {
            User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            if (!Role.getRoleByName(user.getRole()).equals(Role.STORE)) throw new BaseException(NO_STORE_ROLE);

            Store store = storeRepository.findByUserAndStatus(user, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_STORE_IDX));

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
     * [판매자] 상품 삭제
     */
    public void deleteDessert(Long userIdx, Long dessertIdx) throws BaseException {
        try {
            checkStore(userIdx);
            dessertRepository.findByDessertIdxAndStatus(dessertIdx, INACTIVE_STATUS).orElseThrow(() -> new BaseException(DELETED_DESSERT));
            dessertRepository.deleteById(dessertIdx);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * [판매자] 상품 수정
     * 상품 이미지, 이름, 가격, 소개
     */
    public void modifyDessert(PatchDessertReq patchDessertReq, Long dessertIdx, Long userIdx) throws BaseException {
        try {
            checkStore(userIdx);
            Dessert dessert = dessertRepository.findByDessertIdxAndStatus(dessertIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_DESSERT_IDX));

            if (patchDessertReq.getDessertImg() != null) {
                if (!patchDessertReq.getDessertImg().equals("") && !patchDessertReq.getDessertImg().equals(" "))
                    dessert.setDessertImg(patchDessertReq.getDessertImg());
                else throw new BaseException(NULL_DESSERT_IMG);
            }

            if (patchDessertReq.getDessertName() != null) {
                if (!patchDessertReq.getDessertName().equals("") && !patchDessertReq.getDessertName().equals(" "))
                    dessert.setDessertName(patchDessertReq.getDessertName());
                else throw new BaseException(NULL_DESSERT_NAME);
            }

            if (patchDessertReq.getDessertPrice() != null) {
                if (patchDessertReq.getDessertPrice() >= 0)
                    dessert.setDessertPrice(patchDessertReq.getDessertPrice());
                else throw new BaseException(INVALID_DESERT_PRICE);
            }

            if (patchDessertReq.getDessertDescription() != null) {
                if (!patchDessertReq.getDessertDescription().equals("") && !patchDessertReq.getDessertDescription().equals(" "))
                    dessert.setDessertDescription(patchDessertReq.getDessertDescription());
                else throw new BaseException(NULL_DESSERT_DESCRIPTION);
            }
            dessertRepository.save(dessert);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * [판매자] 상품 상세 조회
     * 상품 이미지, 이름, 가격, 소개
     */
    public GetStoreDessertRes getStoreDessert(Long userIdx, Long dessertIdx) throws BaseException {
        try {
            checkStore(userIdx);
            Dessert dessert = dessertRepository.findByDessertIdxAndStatus(dessertIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_DESSERT_IDX));

            return new GetStoreDessertRes(dessert.getStore().getUser().getNickname(), dessert.getDessertImg(), dessert.getDessertName(), dessert.getDessertPrice(), dessert.getDessertDescription());
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private void checkStore(Long userIdx) throws BaseException {
        try {
            User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            if (!Role.getRoleByName(user.getRole()).equals(Role.STORE)) throw new BaseException(NO_STORE_ROLE);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}