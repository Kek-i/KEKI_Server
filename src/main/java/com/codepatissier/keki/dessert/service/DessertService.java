package com.codepatissier.keki.dessert.service;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.Role;
import com.codepatissier.keki.dessert.dto.*;
import com.codepatissier.keki.dessert.entity.Dessert;
import com.codepatissier.keki.dessert.entity.Option;
import com.codepatissier.keki.dessert.repository.DessertRepository;
import com.codepatissier.keki.dessert.repository.OptionRepository;
import com.codepatissier.keki.post.entity.PostImg;
import com.codepatissier.keki.post.repository.PostRepository;
import com.codepatissier.keki.store.entity.Store;
import com.codepatissier.keki.store.repository.StoreRepository;
import com.codepatissier.keki.user.entity.User;
import com.codepatissier.keki.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codepatissier.keki.common.BaseResponseStatus.*;
import static com.codepatissier.keki.common.Constant.ACTIVE_STATUS;

@Service
@RequiredArgsConstructor
public class DessertService {
    private final DessertRepository dessertRepository;
    private final StoreRepository storeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final OptionRepository optionRepository;

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

            ArrayList<GetDessertRes.Image> imgList = new ArrayList<>();
            imgList.add(getDessertImg(dessert));
            ArrayList<GetDessertRes.Image> postImgList = getPostImgList(dessert);
            imgList.addAll(postImgList);

            List<OptionDTO> optionList = getOptionList(dessert);

            // nickname, dessertName, dessertPrice, dessertDescription, imgList(상품 상세 이미지 1장, 피드 이미지 4장), optionList(description, price)
            return new GetDessertRes(dessert.getStore().getUser().getNickname(), dessert.getDessertName(), dessert.getDessertPrice(), dessert.getDessertDescription(), imgList, optionList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private GetDessertRes.Image getDessertImg(Dessert dessert) {
        return new GetDessertRes.Image(dessert.getDessertIdx(), dessert.getDessertImg());
    }

    // 특정 dessert가 들어간 post 4개를 가져와 postIdx와 대표 postImg를 반환
    private ArrayList<GetDessertRes.Image> getPostImgList(Dessert dessert) {
        return (ArrayList<GetDessertRes.Image>) postRepository.findTop4ByDessertAndStatusOrderByPostIdxDesc(dessert, ACTIVE_STATUS).stream()
                .map(posts -> new GetDessertRes.Image(posts.getPostIdx(),
                        representPostImgUrl(posts.getImages()))).collect(Collectors.toList());
    }

    private String representPostImgUrl(List<PostImg> postImages){
        return postImages.isEmpty() ? null : postImages.get(0).getImgUrl();
    }

    private List<OptionDTO> getOptionList(Dessert dessert) {
        return optionRepository.findByDessertAndStatusOrderByOptionIdx(dessert, ACTIVE_STATUS).stream()
                .map(option -> new OptionDTO(option.getOptionIdx(), option.getDescription(), option.getPrice())).collect(Collectors.toList());
    }

    /**
     * [판매자] 상품 등록
     * 상품 이름, 가격, 소개, 이미지(1장)
     */
    @Transactional(rollbackFor = Exception.class)
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
            dessertRepository.findByDessertIdxAndStatus(dessertIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(DELETED_DESSERT));
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
    @Transactional(rollbackFor = Exception.class)
    public void modifyDessert(PatchDessertReq patchDessertReq, Long dessertIdx, Long userIdx) throws BaseException {
        try {
            checkStore(userIdx);
            Dessert dessert = dessertRepository.findByDessertIdxAndStatus(dessertIdx,ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_DESSERT_IDX));

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

            if (!patchDessertReq.getOptions().isEmpty() && !(patchDessertReq.getOptions() == null)) {
                for (OptionDTO optionDTO : patchDessertReq.getOptions()) {
                    if(optionDTO.getOptionIdx() == null)
                        throw new BaseException(NULL_OPTION_IDX);
                    if(optionDTO.getOptionDescription() == null)
                        throw new BaseException(NULL_OPTION_DESCRIPTION);
                    if(optionDTO.getOptionPrice() == null)
                        throw new BaseException(NULL_OPTION_PRICE);
                }

                List<Long> optionIdxList = new ArrayList<>();
                for (OptionDTO optionDTO : patchDessertReq.getOptions()) {
                    optionIdxList.add(optionDTO.getOptionIdx());
                }

                for (Long optionIdx : optionIdxList) {
                    Option option = optionRepository.findByOptionIdxAndStatus(optionIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_OPTION_IDX));

                    for(OptionDTO modifiedOption : patchDessertReq.getOptions()) {
                        option.setDescription(modifiedOption.getOptionDescription());
                        option.setPrice(modifiedOption.getOptionPrice());
                    }
                    optionRepository.save(option);
                }
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
     * 가게 이름, 상품 이미지, 상품명, 상품 가격, 상품 설명, 옵션(optionIdx, 옵션명, 옵션 가격)
     */
    public GetStoreDessertRes getStoreDessert(Long userIdx, Long dessertIdx) throws BaseException {
        try {
            checkStore(userIdx);
            Dessert dessert = dessertRepository.findByDessertIdxAndStatus(dessertIdx, ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_DESSERT_IDX));

            List<OptionDTO> optionList = getOptionList(dessert);
            return new GetStoreDessertRes(dessert.getStore().getUser().getNickname(), dessert.getDessertImg(), dessert.getDessertName(), dessert.getDessertPrice(), dessert.getDessertDescription(), optionList);
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