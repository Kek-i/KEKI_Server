package com.codepatissier.keki.post.service;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.post.dto.GetStorePostsRes;
import com.codepatissier.keki.post.entity.PostImg;
import com.codepatissier.keki.post.repository.PostRepository;
import com.codepatissier.keki.stores.entity.Store;
import com.codepatissier.keki.stores.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.codepatissier.keki.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final StoreRepository storeRepository;

    public GetStorePostsRes getPostList(Long storeIdx, Long cursorIdx, Pageable page) throws BaseException {
        try{
            Store store = this.storeRepository.findById(storeIdx)
                    .orElseThrow(() -> new BaseException(INVALID_STORE_IDX));

            List<GetStorePostsRes.Feed> postList = cursorIdx == null?
                    this.getItemList(store, page):
                    this.getItemListWithCursor(store, cursorIdx, page);

            Long lastIdxOfList = getLastIdxOfList(postList);
            return new GetStorePostsRes(postList, lastIdxOfList, hasNext(lastIdxOfList));
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 최초 조회
     */
    private List<GetStorePostsRes.Feed> getItemList(Store store, Pageable page) {
        return this.postRepository.findByStoreOrderByPostIdxDesc(store, page).stream()
                .map(post -> new GetStorePostsRes.Feed(post.getPostIdx(),
                        this.representPostImgUrl(post.getImages()))).collect(Collectors.toList());
    }

    /**
     * 최초 아닌 조회
     */
    private List<GetStorePostsRes.Feed> getItemListWithCursor(Store store, Long cursorIdx, Pageable page) throws BaseException {
        this.postRepository.findById(cursorIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));

        return this.postRepository.findByStoreAndPostIdxLessThanOrderByPostIdxDesc(store, cursorIdx, page).stream()
                .map(post -> new GetStorePostsRes.Feed(post.getPostIdx(),
                        this.representPostImgUrl(post.getImages()))).collect(Collectors.toList());
    }

    /**
     * @return 피드 대표사진
     */
    private String representPostImgUrl(List<PostImg> postImgs){
        return postImgs.isEmpty() ?
                null : postImgs.get(0).getImgUrl();
    }

    /**
     * @return 피드 목록의 마지막 피드 idx
     */
    private static Long getLastIdxOfList(List<GetStorePostsRes.Feed> postList) {
        return postList.isEmpty() ?
                null : postList.get(postList.size() - 1).getPostIdx();
    }

    /**
     * @return 다음에 조회될 피드 존재 여부
     */
    private Boolean hasNext(Long lastIdx) {
        return lastIdx != null && this.postRepository.existsByPostIdxLessThan(lastIdx);
    }
}
