package com.codepatissier.keki.post.service;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.post.dto.GetPostsRes;
import com.codepatissier.keki.post.entity.Post;
import com.codepatissier.keki.post.entity.PostImg;
import com.codepatissier.keki.post.repository.PostLikeRepository;
import com.codepatissier.keki.post.repository.PostRepository;
import com.codepatissier.keki.store.entity.Store;
import com.codepatissier.keki.store.repository.StoreRepository;
import com.codepatissier.keki.user.entity.User;
import com.codepatissier.keki.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.codepatissier.keki.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    /**
     * 스토어별 조회
     */
    public GetPostsRes getPostList(Long storeIdx, Long cursorIdx, Pageable page) throws BaseException {
        try{
            Store store = this.storeRepository.findById(storeIdx)
                    .orElseThrow(() -> new BaseException(INVALID_STORE_IDX));

            // TODO: 토큰 완료되면 수정
            User user =this.userRepository.findById(1L).orElse(null);

            List<GetPostsRes.Feed> postList = cursorIdx == null?
                    this.getFeedList(store, user, page):
                    this.getFeedListWithCursor(store, user, cursorIdx, page);

            Long lastIdxOfList = getLastIdxOfList(postList);
            return new GetPostsRes(postList, lastIdxOfList, hasNext(store,lastIdxOfList));
        } catch (BaseException e){
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 검색 조회
     */
    public GetPostsRes getSearchPostList(String searchWord, Long cursorIdx, Pageable page) throws BaseException {
        try{
            // TODO: 토큰 완료되면 수정
            User user =this.userRepository.findById(1L).orElse(null);

            List<GetPostsRes.Feed> postList = cursorIdx == null?
                    this.getSearchFeedList(searchWord, user, page):
                    this.getSearchFeedListWithCursor(searchWord, user, cursorIdx, page);

            Long lastIdxOfList = getLastIdxOfList(postList);
            return new GetPostsRes(postList, lastIdxOfList, hasNext(searchWord, lastIdxOfList));
        } catch (BaseException e){
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 최초 조회
     */
    private List<GetPostsRes.Feed> getFeedList(Store store, User user, Pageable page) {
        return this.postRepository.findByStoreOrderByPostIdxDesc(store, page).stream()
                .map(getFeedFunction(user))
                .collect(Collectors.toList());
    }

    /**
     * 최초 아닌 조회
     */
    private List<GetPostsRes.Feed> getFeedListWithCursor(Store store, User user, Long cursorIdx, Pageable page) throws BaseException {
        this.postRepository.findById(cursorIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));

        return this.postRepository.findByStoreAndPostIdxLessThanOrderByPostIdxDesc(store, cursorIdx, page).stream()
                .map(getFeedFunction(user))
                .collect(Collectors.toList());
    }

    /**
     * 최초 조회
     */
    private List<GetPostsRes.Feed> getSearchFeedList(String searchWord, User user, Pageable page) {
        return this.postRepository.findByDessert_DessertNameContainingOrderByPostIdxDesc(searchWord, page).stream()
                .map(getFeedFunction(user))
                .collect(Collectors.toList());
    }

    /**
     * 최초 아닌 조회
     */
    private List<GetPostsRes.Feed> getSearchFeedListWithCursor(String searchWord, User user, Long cursorIdx, Pageable page) throws BaseException {
        this.postRepository.findById(cursorIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));

        return this.postRepository.findByDessert_DessertNameContainingAndPostIdxLessThanOrderByPostIdxDesc(searchWord, cursorIdx, page).stream()
                .map(getFeedFunction(user))
                .collect(Collectors.toList());
    }

    /**
     * Feed 만들기
     */
    private Function<Post, GetPostsRes.Feed> getFeedFunction(User user) {
        return post -> new GetPostsRes.Feed(
                post.getPostIdx(),
                post.getDessert().getDessertName(),
                post.getPostDescription(),
                post.getImages().stream().map(PostImg::getImgUrl).collect(Collectors.toList()),
                post.getTags().stream().map(postTag -> postTag.getTag().getTagName()).collect(Collectors.toList()),
                post.getStore().getBrandName(),
                post.getStore().getUser().getProfileImg(),
                this.postLikeRepository.existsByPostAndUser(post, user));
    }

    /**
     * @return 피드 목록의 마지막 피드 idx
     */
    private static Long getLastIdxOfList(List<GetPostsRes.Feed> postList) {
        return postList.isEmpty() ?
                null : postList.get(postList.size() - 1).getPostIdx();
    }

    /**
     * @return 다음에 조회될 피드 존재 여부
     */
    private Boolean hasNext(Store store, Long lastIdx) {
        return lastIdx != null && this.postRepository.existsByStoreAndPostIdxLessThan(store, lastIdx);
    }

    /**
     * @return 다음에 조회될 피드 존재 여부
     */
    private Boolean hasNext(String searchWord, Long lastIdx) {
        return lastIdx != null && this.postRepository.existsByDessert_DessertNameContainingAndPostIdxLessThan(searchWord, lastIdx);
    }
}
