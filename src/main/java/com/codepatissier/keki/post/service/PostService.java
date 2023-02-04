package com.codepatissier.keki.post.service;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.Role;
import com.codepatissier.keki.common.Tag.Tag;
import com.codepatissier.keki.common.Tag.TagRepository;
import com.codepatissier.keki.dessert.entity.Dessert;
import com.codepatissier.keki.dessert.repository.DessertRepository;
import com.codepatissier.keki.history.entity.PostHistory;
import com.codepatissier.keki.history.entity.SearchHistory;
import com.codepatissier.keki.history.repository.PostHistoryRepository;
import com.codepatissier.keki.history.repository.SearchHistoryRepository;
import com.codepatissier.keki.post.dto.GetPostRes;
import com.codepatissier.keki.post.dto.GetPostsRes;
import com.codepatissier.keki.post.dto.PostPostReq;
import com.codepatissier.keki.post.entity.PostTag;
import com.codepatissier.keki.cs.entity.Report;
import com.codepatissier.keki.cs.entity.ReportCategory;
import com.codepatissier.keki.cs.repository.ReportRepository;
import com.codepatissier.keki.post.dto.PostReportReq;
import com.codepatissier.keki.post.entity.Post;
import com.codepatissier.keki.post.entity.PostImg;
import com.codepatissier.keki.post.entity.PostLike;
import com.codepatissier.keki.post.repository.PostImgRepository;
import com.codepatissier.keki.post.repository.PostLikeRepository;
import com.codepatissier.keki.post.repository.PostRepository;
import com.codepatissier.keki.post.repository.PostTagRepository;
import com.codepatissier.keki.store.entity.Store;
import com.codepatissier.keki.store.repository.StoreRepository;
import com.codepatissier.keki.user.entity.User;
import com.codepatissier.keki.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.codepatissier.keki.common.BaseResponseStatus.*;
import static com.codepatissier.keki.common.Constant.*;
import static com.codepatissier.keki.common.Constant.Posts.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final ReportRepository reportRepository;
    private final PostHistoryRepository postHistoryRepository;
    private final DessertRepository dessertRepository;
    private final PostImgRepository postImgRepository;

    /**
     * 신고하기
     */
    public void doReport(Long userIdx, PostReportReq postReportReq, Long postIdx) throws BaseException {
        try {
            User user = this.userRepository.findById(userIdx)
                    .orElseThrow(() -> new BaseException(INVALID_USER_IDX));

            Post post = this.postRepository.findById(postIdx)
                    .orElseThrow(() -> new BaseException(INVALID_POST_IDX));

            Report report = Report.builder()
                    .user(user)
                    .reportCategory(ReportCategory.getReportCategoryByName(postReportReq.getReportName()))
                    .post(post)
                    .build();
            this.reportRepository.save(report);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 게시물 좋아요
     */
    public void doLike(Long userIdx, Long postIdx) throws BaseException {
        try {
            User user = this.userRepository.findById(userIdx)
                    .orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            Post post = this.postRepository.findById(postIdx)
                    .orElseThrow(() -> new BaseException(INVALID_POST_IDX));
            PostLike postLike = this.postLikeRepository.findByPostAndUser(post, user);

            if (postLike != null){
                if(postLike.getStatus().equals(ACTIVE_STATUS))
                    postLike.setStatus(INACTIVE_STATUS);
                else if (postLike.getStatus().equals(INACTIVE_STATUS))
                    postLike.setStatus(ACTIVE_STATUS);
            } else {
                postLike = PostLike.builder()
                        .user(user)
                        .post(post)
                        .build();
            }
            this.postLikeRepository.save(postLike);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 게시물 조회 기록
     */
    public void makePostHistory(Long userIdx, Long postIdx) throws BaseException {
        try {
            User user = this.userRepository.findById(userIdx).orElse(null);
            if (user == null) return;
            Post post = this.postRepository.findById(postIdx)
                    .orElseThrow(() -> new BaseException(INVALID_POST_IDX));
            PostHistory postHistory = PostHistory.builder()
                    .post(post)
                    .user(user)
                    .build();

            this.postHistoryRepository.save(postHistory);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 게시물 삭제
     */
    public void deletePost(Long userIdx, Long postIdx) throws BaseException {
        try {
            User user = this.userRepository.findById(userIdx)
                    .orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            if (!Role.getRoleByName(user.getRole()).equals(Role.STORE))
                throw new BaseException(NO_STORE_ROLE);

            Post post = this.postRepository.findById(postIdx)
                    .orElseThrow(() -> new BaseException(INVALID_POST_IDX));
            if(!user.getUserIdx().equals(post.getStore().getUser().getUserIdx()))
                throw new BaseException(NO_MATCH_POST_STORE);

            post.setStatus(INACTIVE_STATUS);

            this.postRepository.save(post);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 게시물 등록
     */
    @Transactional(rollbackFor= Exception.class)
    public void makePost(Long userIdx, PostPostReq postPostReq) throws BaseException {
        try {
            User user = this.userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE_STATUS)
                    .orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            if (!Role.getRoleByName(user.getRole()).equals(Role.STORE))
                throw new BaseException(NO_STORE_ROLE);

            Store store = this.storeRepository.findByUser(user)
                    .orElseThrow(()->new BaseException(INVALID_STORE_IDX));
            Dessert dessert = this.dessertRepository.findById(postPostReq.getDessertIdx())
                    .orElseThrow(() -> new BaseException(INVALID_DESSERT_IDX));

            Post post = savePost(postPostReq, store, dessert);
            savePostTags(postPostReq, post);
            savePostImgs(postPostReq, post);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private void savePostImgs(PostPostReq postPostReq, Post post) {
        List<PostImg> postImgs = postPostReq.getPostImgUrls().stream()
                .map(url -> PostImg.builder()
                        .post(post)
                        .imgUrl(url)
                        .build())
                .collect(Collectors.toList());
        this.postImgRepository.saveAll(postImgs);
    }

    private void savePostTags(PostPostReq postPostReq, Post post) throws BaseException {
        List<PostTag> postTags = new ArrayList<>();
        for (String tagName: postPostReq.getTags()) {
            postTags.add(PostTag.builder()
                    .post(post)
                    .tag(this.tagRepository.findByTagName(tagName)
                            .orElseThrow(()->new BaseException(INVALID_TAG)))
                    .build());
        }
        this.postTagRepository.saveAll(postTags);
    }

    private Post savePost(PostPostReq postPostReq, Store store, Dessert dessert) {
        Post post = Post.builder()
                .dessert(dessert)
                .store(store)
                .postDescription(postPostReq.getDescription())
                .build();
        this.postRepository.save(post);
        return post;
    }

    /**
     * 스토어별 조회
     */
    public GetPostsRes getPosts(Long userIdx, Long storeIdx, Long cursorIdx, Pageable page) throws BaseException {
        try{
            Store store = this.storeRepository.findById(storeIdx)
                    .orElseThrow(() -> new BaseException(INVALID_STORE_IDX));
            User user =this.userRepository.findById(userIdx).orElse(null);

            List<GetPostRes> postList = cursorIdx == null?
                    this.getFeeds(store, user, page):
                    this.getFeedsWithCursor(store, user, cursorIdx, page);

            Long lastIdxOfList = getLastIdxOfList(postList);
            int numOfRows = postList.size();
            return new GetPostsRes(postList, lastIdxOfList, hasNext(store,lastIdxOfList), numOfRows);
        } catch (BaseException e){
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 검색 조회
     */
    public GetPostsRes getSearchPosts(Long userIdx, String searchWord, String sortType, Long cursorIdx, Pageable page) throws BaseException {
        try{
            User user =this.userRepository.findById(userIdx).orElse(null);

            List<GetPostRes> postList = cursorIdx == null?
                    this.getSearchFeeds(searchWord, user, sortType, page):
                    this.getSearchFeedsWithCursor(searchWord, user, sortType, cursorIdx, page);

            if (user != null) {
                SearchHistory searchHistory = SearchHistory.builder()
                        .user(user)
                        .searchWord(searchWord)
                        .build();
                this.searchHistoryRepository.save(searchHistory);
            }

            Long lastIdxOfList = getLastIdxOfList(postList);
            int numOfRows = postList.size();
            return new GetPostsRes(postList, lastIdxOfList, hasNext(searchWord, lastIdxOfList), numOfRows);
        } catch (BaseException e){
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 태그 검색 조회
     */
    public GetPostsRes getPostsByTag(Long userIdx, String searchTag, String sortType, Long cursorIdx, Pageable page) throws BaseException {
        try{
            Tag tag = this.tagRepository.findByTagName(searchTag)
                    .orElseThrow(() -> new BaseException(INVALID_TAG));
            User user = this.userRepository.findById(userIdx).orElse(null);

            List<GetPostRes> postList = cursorIdx == null?
                    this.getFeedsByTag(tag, user, sortType, page):
                    this.getFeedsByTagWithCursor(tag, user, sortType, cursorIdx, page);

            Long lastIdxOfList = getLastIdxOfList(postList);
            int numOfRows = postList.size();
            return new GetPostsRes(postList, lastIdxOfList, hasNext(tag, lastIdxOfList), numOfRows);
        } catch (BaseException e){
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 스토어별 최초 조회
     */
    private List<GetPostRes> getFeeds(Store store, User user, Pageable page) throws BaseException {
        return this.postRepository.findByStoreAndStatusOrderByPostIdxDesc(store, ACTIVE_STATUS, page).stream()
                .map(getFeedFunction(user))
                .collect(Collectors.toList());
    }

    /**
     * 검색어별 최초 조회
     */
    private List<GetPostRes> getSearchFeeds(String searchWord, User user, String sortType, Pageable page) throws BaseException {
        switch (sortType) {
            case NEW_SORT_TYPE:
                return this.postRepository.findByDessertDessertNameContainingAndStatusOrderByPostIdxDesc(searchWord, ACTIVE_STATUS, page).stream()
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
            case POPULAR_SORT_TYPE:
                return this.postRepository.getPopularSortSearchPosts(searchWord, page).stream()
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
            case LOW_PRICE_SORT_TYPE:
                return this.postRepository.findByDessertDessertNameContainingAndStatusOrderByDessertDessertPriceAscPostIdxDesc(searchWord, ACTIVE_STATUS, page).stream()
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
            default:
                throw new BaseException(INVALID_SORT_TYPE);
        }
    }

    /**
     * 태그별 최초 조회
     */
    private List<GetPostRes> getFeedsByTag(Tag tag, User user, String sortType, Pageable page) throws BaseException {
        switch (sortType) {
            case NEW_SORT_TYPE:
                return this.postTagRepository.findByTagAndPostStatusOrderByPostPostIdxDesc(tag, ACTIVE_STATUS, page).stream()
                        .map(PostTag::getPost)
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
            case POPULAR_SORT_TYPE:
                return this.postTagRepository.getPopularSortTagPosts(tag, page).stream()
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
            case LOW_PRICE_SORT_TYPE:
                return this.postTagRepository.getLowPriceSortTagPosts(tag, page).stream()
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
            default:
                throw new BaseException(INVALID_SORT_TYPE);
        }
    }

    /**
     * 스토어별 최초 아닌 조회
     */
    private List<GetPostRes> getFeedsWithCursor(Store store, User user, Long cursorIdx, Pageable page) throws BaseException {
        this.postRepository.findById(cursorIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));
        return this.postRepository.findByStoreAndStatusAndPostIdxLessThanOrderByPostIdxDesc(store, ACTIVE_STATUS, cursorIdx, page).stream()
                .map(getFeedFunction(user))
                .collect(Collectors.toList());
    }

    /**
     * 검색어별 최초 아닌 조회
     */
    private List<GetPostRes> getSearchFeedsWithCursor(String searchWord, User user, String sortType, Long cursorIdx, Pageable page) throws BaseException {
        this.postRepository.findById(cursorIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));
        switch (sortType) {
            case NEW_SORT_TYPE:
                return this.postRepository.findByDessertDessertNameContainingAndStatusAndPostIdxLessThanOrderByPostIdxDesc(searchWord, ACTIVE_STATUS, cursorIdx, page).stream()
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
            case POPULAR_SORT_TYPE:
                return this.postRepository.getPopularSortSearchPostsWithCursor(searchWord, cursorIdx, page).stream()
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
            case LOW_PRICE_SORT_TYPE:
                return this.postRepository.findByDessertDessertNameContainingAndStatusAndPostIdxLessThanOrderByDessertDessertPriceAscPostIdxDesc(searchWord, ACTIVE_STATUS, cursorIdx, page).stream()
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
            default:
                throw new BaseException(INVALID_SORT_TYPE);
        }
    }

    /**
     * 태그별 최초 아닌 조회
     */
    private List<GetPostRes> getFeedsByTagWithCursor(Tag tag, User user, String sortType, Long cursorIdx, Pageable page) throws BaseException {
        this.postRepository.findById(cursorIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));

        switch (sortType) {
            case NEW_SORT_TYPE:
                return this.postTagRepository.findByTagAndPostStatusAndPostPostIdxLessThanOrderByPostPostIdxDesc(tag, ACTIVE_STATUS, cursorIdx, page).stream()
                        .map(PostTag::getPost)
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
            case POPULAR_SORT_TYPE:
                return this.postTagRepository.getPopularSortTagPostsWithCursor(tag, cursorIdx, page).stream()
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
            case LOW_PRICE_SORT_TYPE:
                return this.postTagRepository.getLowPriceSortTagPostsWithCursor(tag, cursorIdx, page).stream()
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
            default:
                throw new BaseException(INVALID_SORT_TYPE);
        }
    }

    /**
     * Feed 만들기
     */
    private Function<Post, GetPostRes> getFeedFunction(User user) {
        return post -> new GetPostRes(
                post,
                this.postLikeRepository.existsByPostAndUserAndStatus(post, user, ACTIVE_STATUS));
    }

    /**
     * @return 피드 목록의 마지막 피드 idx
     */
    private static Long getLastIdxOfList(List<GetPostRes> postList) {
        return postList.isEmpty() ?
                null : postList.get(postList.size() - 1).getPostIdx();
    }

    /**
     * 스토어별
     * @return 다음에 조회될 피드 존재 여부
     */
    private Boolean hasNext(Store store, Long lastIdx) {
        return lastIdx != null && this.postRepository.existsByStoreAndStatusAndPostIdxLessThan(store, ACTIVE_STATUS, lastIdx);
    }

    /**
     * 검색어별
     * @return 다음에 조회될 피드 존재 여부
     */
    private Boolean hasNext(String searchWord, Long lastIdx) {
        return lastIdx != null && this.postRepository.existsByDessertDessertNameContainingAndStatusAndPostIdxLessThan(searchWord, ACTIVE_STATUS, lastIdx);
    }

    /**
     * 태그별
     * @return 다음에 조회될 피드 존재 여부
     */
    private Boolean hasNext(Tag tag, Long lastIdx) {
        return lastIdx != null && this.postTagRepository.existsByTagAndPostPostIdxLessThan(tag, lastIdx);
    }
}
