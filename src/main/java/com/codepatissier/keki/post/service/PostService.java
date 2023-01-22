package com.codepatissier.keki.post.service;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.Role;
import com.codepatissier.keki.common.Tag.Tag;
import com.codepatissier.keki.common.Tag.TagRepository;
import com.codepatissier.keki.history.entity.PostHistory;
import com.codepatissier.keki.history.entity.SearchHistory;
import com.codepatissier.keki.history.repository.PostHistoryRepository;
import com.codepatissier.keki.history.repository.SearchHistoryRepository;
import com.codepatissier.keki.post.dto.GetPostsRes;
import com.codepatissier.keki.post.entity.PostTag;
import com.codepatissier.keki.common.Constant;
import com.codepatissier.keki.cs.entity.Report;
import com.codepatissier.keki.cs.entity.ReportCategory;
import com.codepatissier.keki.cs.repository.ReportRepository;
import com.codepatissier.keki.post.dto.PostReportReq;
import com.codepatissier.keki.post.entity.Post;
import com.codepatissier.keki.post.entity.PostImg;
import com.codepatissier.keki.post.entity.PostLike;
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

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.codepatissier.keki.common.BaseResponseStatus.*;
import static com.codepatissier.keki.common.Constant.INACTIVE_STATUS;

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
                if(postLike.getStatus().equals(Constant.ACTIVE_STATUS))
                    postLike.setStatus(Constant.INACTIVE_STATUS);
                else if (postLike.getStatus().equals(Constant.INACTIVE_STATUS))
                    postLike.setStatus(Constant.ACTIVE_STATUS);
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
     * 스토어별 조회
     */
    public GetPostsRes getPosts(Long userIdx, Long storeIdx, Long cursorIdx, Pageable page) throws BaseException {
        try{
            Store store = this.storeRepository.findById(storeIdx)
                    .orElseThrow(() -> new BaseException(INVALID_STORE_IDX));
            User user =this.userRepository.findById(userIdx).orElse(null);

            List<GetPostsRes.Feed> postList = cursorIdx == null?
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
    public GetPostsRes getSearchPosts(Long userIdx, String searchWord, Long cursorIdx, Pageable page) throws BaseException {
        try{
            User user =this.userRepository.findById(userIdx).orElse(null);

            List<GetPostsRes.Feed> postList = cursorIdx == null?
                    this.getSearchFeeds(searchWord, user, page):
                    this.getSearchFeedsWithCursor(searchWord, user, cursorIdx, page);

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
    public GetPostsRes getPostsByTag(Long userIdx, String searchTag, Long cursorIdx, Pageable page) throws BaseException {
        try{
            Tag tag = this.tagRepository.findByTagName(searchTag)
                    .orElseThrow(() -> new BaseException(INVALID_TAG_IDX));
            User user = this.userRepository.findById(userIdx).orElse(null);

            List<GetPostsRes.Feed> postList = cursorIdx == null?
                    this.getFeedsByTag(tag, user, page):
                    this.getFeedsByTagWithCursor(tag, user, cursorIdx, page);

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
    private List<GetPostsRes.Feed> getFeeds(Store store, User user, Pageable page) {
        return this.postRepository.findByStoreOrderByPostIdxDesc(store, page).stream()
                .map(getFeedFunction(user))
                .collect(Collectors.toList());
    }

    /**
     * 검색어별 최초 조회
     */
    private List<GetPostsRes.Feed> getSearchFeeds(String searchWord, User user, Pageable page) {
        return this.postRepository.findByDessertDessertNameContainingOrderByPostIdxDesc(searchWord, page).stream()
                .map(getFeedFunction(user))
                .collect(Collectors.toList());
    }

    /**
     * 태그별 최초 조회
     */
    private List<GetPostsRes.Feed> getFeedsByTag(Tag tag, User user, Pageable page) {
        return this.postTagRepository.findByTagOrderByPostPostIdxDesc(tag, page).stream()
                .map(PostTag::getPost)
                .map(getFeedFunction(user))
                .collect(Collectors.toList());
    }

    /**
     * 스토어별 최초 아닌 조회
     */
    private List<GetPostsRes.Feed> getFeedsWithCursor(Store store, User user, Long cursorIdx, Pageable page) throws BaseException {
        this.postRepository.findById(cursorIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));

        return this.postRepository.findByStoreAndPostIdxLessThanOrderByPostIdxDesc(store, cursorIdx, page).stream()
                .map(getFeedFunction(user))
                .collect(Collectors.toList());
    }

    /**
     * 검색어별 최초 아닌 조회
     */
    private List<GetPostsRes.Feed> getSearchFeedsWithCursor(String searchWord, User user, Long cursorIdx, Pageable page) throws BaseException {
        this.postRepository.findById(cursorIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));

        return this.postRepository.findByDessertDessertNameContainingAndPostIdxLessThanOrderByPostIdxDesc(searchWord, cursorIdx, page).stream()
                .map(getFeedFunction(user))
                .collect(Collectors.toList());
    }

    /**
     * 태그별 최초 아닌 조회
     */
    private List<GetPostsRes.Feed> getFeedsByTagWithCursor(Tag tag, User user, Long cursorIdx, Pageable page) throws BaseException {
        this.postRepository.findById(cursorIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));

        return this.postTagRepository.findByTagAndPostPostIdxLessThanOrderByPostPostIdxDesc(tag, cursorIdx, page).stream()
                .map(PostTag::getPost)
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
                post.getDessert().getDessertPrice(),
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
     * 스토어별
     * @return 다음에 조회될 피드 존재 여부
     */
    private Boolean hasNext(Store store, Long lastIdx) {
        return lastIdx != null && this.postRepository.existsByStoreAndPostIdxLessThan(store, lastIdx);
    }

    /**
     * 검색어별
     * @return 다음에 조회될 피드 존재 여부
     */
    private Boolean hasNext(String searchWord, Long lastIdx) {
        return lastIdx != null && this.postRepository.existsByDessertDessertNameContainingAndPostIdxLessThan(searchWord, lastIdx);
    }

    /**
     * 태그별
     * @return 다음에 조회될 피드 존재 여부
     */
    private Boolean hasNext(Tag tag, Long lastIdx) {
        return lastIdx != null && this.postTagRepository.existsByTagAndPostPostIdxLessThan(tag, lastIdx);
    }
}
