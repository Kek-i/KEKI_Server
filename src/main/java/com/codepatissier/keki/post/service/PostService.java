package com.codepatissier.keki.post.service;

import com.codepatissier.keki.calendar.dto.TagRes;
import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.Constant;
import com.codepatissier.keki.common.Role;
import com.codepatissier.keki.common.Tag.Tag;
import com.codepatissier.keki.common.Tag.TagRepository;
import com.codepatissier.keki.dessert.entity.Dessert;
import com.codepatissier.keki.dessert.repository.DessertRepository;
import com.codepatissier.keki.history.entity.PostHistory;
import com.codepatissier.keki.history.entity.SearchHistory;
import com.codepatissier.keki.history.repository.PostHistoryRepository;
import com.codepatissier.keki.history.repository.SearchHistoryRepository;
import com.codepatissier.keki.post.dto.*;
import com.codepatissier.keki.post.entity.PostTag;
import com.codepatissier.keki.cs.entity.Report;
import com.codepatissier.keki.cs.entity.ReportCategory;
import com.codepatissier.keki.cs.repository.ReportRepository;
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

import java.time.LocalDateTime;
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
     * ????????????
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
     * ????????? ?????????
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
     * ????????? ?????? ??????
     */
    public void makePostHistory(Long userIdx, Long postIdx) throws BaseException {
        try {
            User user = this.userRepository.findById(userIdx).orElse(null);
            if (user == null) return;
            Post post = this.postRepository.findById(postIdx)
                    .orElseThrow(() -> new BaseException(INVALID_POST_IDX));

            savePostHistory(user, post);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private void savePostHistory(User user, Post post) {
        PostHistory postHistory = PostHistory.builder()
                .post(post)
                .user(user)
                .build();
        this.postHistoryRepository.save(postHistory);
    }

    /**
     * ????????? ??????
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
     * ????????? ?????? ??????
     */
    public GetModifyPostRes getModifyPostView(Long userIdx, Long postIdx) throws BaseException {
        try {
            User user = this.userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE_STATUS)
                    .orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            if (!Role.getRoleByName(user.getRole()).equals(Role.STORE))
                throw new BaseException(NO_STORE_ROLE);
            Store store = this.storeRepository.findByUser(user)
                    .orElseThrow(()->new BaseException(INVALID_STORE_IDX));
            Post post = this.postRepository.findById(postIdx)
                    .orElseThrow(() -> new BaseException(INVALID_POST_IDX));

            return new GetModifyPostRes(post, getDefaultDessertsAndTags(store));
        } catch (BaseException e) {
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private GetMakePostRes getDefaultDessertsAndTags(Store store) {
        List<DessertsRes> desserts = this.dessertRepository.findByStoreAndStatusOrderByDessertIdx(store, ACTIVE_STATUS).stream()
                .map(dessert -> new DessertsRes(dessert.getDessertIdx(), dessert.getDessertName()))
                .collect(Collectors.toList());
        List<String> tags = this.tagRepository.findByStatus(Constant.ACTIVE_STATUS).stream()
                .map(Tag::getTagName)
                .collect(Collectors.toList());
        return new GetMakePostRes(desserts, tags);
    }

    /**
     * ????????? ??????
     */
    @Transactional(rollbackFor= Exception.class)
    public void modifyPost(Long userIdx, Long postIdx, PatchPostReq patchPostReq) throws BaseException {
        try {
            User user = this.userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE_STATUS)
                    .orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            if (!Role.getRoleByName(user.getRole()).equals(Role.STORE))
                throw new BaseException(NO_STORE_ROLE);

            Post post = this.postRepository.findById(postIdx)
                    .orElseThrow(() -> new BaseException(INVALID_POST_IDX));
            if(!user.getUserIdx().equals(post.getStore().getUser().getUserIdx()))
                throw new BaseException(NO_MATCH_POST_STORE);

            if(patchPostReq.getDessertIdx() != null){
                Dessert dessert = this.dessertRepository.findById(patchPostReq.getDessertIdx())
                                .orElseThrow(() -> new BaseException(INVALID_DESSERT_IDX));
                post.setDessert(dessert);
            }
            if(patchPostReq.getDescription() != null){
                post.setPostDescription(patchPostReq.getDescription());
            }
            if(patchPostReq.getTags() != null){
                List<PostTag> postTags = new ArrayList<>();
                post.getTags().forEach(postTag -> postTag.setStatus(INACTIVE_STATUS));

                for (String tagStr:patchPostReq.getTags()) {
                    Tag tag = this.tagRepository.findByTagName(tagStr)
                            .orElseThrow(()->new BaseException(INVALID_TAG));
                    PostTag postTag = this.postTagRepository.findByPostAndTag(post, tag);
                    if(postTag != null) postTag.setStatus(ACTIVE_STATUS);
                    else postTags.add(PostTag.builder()
                                .post(post)
                                .tag(tag)
                                .build());
                }
                this.postTagRepository.saveAll(postTags);
            }
            if(patchPostReq.getPostImgUrls() != null){
                List<PostImg> postImgs = new ArrayList<>();
                post.getImages().forEach(postImg -> postImg.setStatus(INACTIVE_STATUS));

                for (String imgUrl:patchPostReq.getPostImgUrls()){
                    PostImg postImg = this.postImgRepository.findByPostAndImgUrl(post, imgUrl);
                    if (postImg != null) postImg.setStatus(ACTIVE_STATUS);
                    else postImgs.add(PostImg.builder()
                            .post(post)
                            .imgUrl(imgUrl)
                            .build());
                }
                this.postImgRepository.saveAll(postImgs);
            }
            this.postRepository.save(post);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * ????????? ?????? ??????
     */
    public GetMakePostRes getMakePostView(Long userIdx) throws BaseException {
        try {
            User user = this.userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE_STATUS)
                    .orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            if (!Role.getRoleByName(user.getRole()).equals(Role.STORE))
                throw new BaseException(NO_STORE_ROLE);

            Store store = this.storeRepository.findByUser(user)
                    .orElseThrow(()->new BaseException(INVALID_STORE_IDX));
            return getDefaultDessertsAndTags(store);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * ????????? ??????
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
     * ?????? ?????? ??????
     */
    public GetPostRes getPost(Long postIdx, Long userIdx) throws BaseException {
        try{
            Post post = this.postRepository.findById(postIdx)
                    .orElseThrow(() -> new BaseException(INVALID_POST_IDX));
            User user = this.userRepository.findById(userIdx).orElse(null);

            GetPostRes feed = new GetPostRes(post, this.postLikeRepository.existsByPostAndUserAndStatus(post, user, ACTIVE_STATUS));
            if(user != null) savePostHistory(user, post);
            return feed;
        } catch (BaseException e){
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetLikePostsRes getLikePosts(Long userIdx, LocalDateTime cursorDate, Pageable page) throws BaseException {
        try{
            User user = this.userRepository.findById(userIdx)
                    .orElseThrow(() -> new BaseException(INVALID_USER_IDX));

            List<GetLikePostRes> postList = cursorDate == null?
                    this.getFeeds(user, page):
                    this.getFeedsWithCursor(user, cursorDate, page);

            LocalDateTime lastDateOfList = getLastDateOfList(postList, user);
            int numOfRows = postList.size();
            return new GetLikePostsRes(postList, lastDateOfList, hasNext(user, lastDateOfList), numOfRows);
        } catch (BaseException e){
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * ???????????? ??????
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
            return new GetPostsRes(postList, lastIdxOfList, null, null, hasNext(store, lastIdxOfList), postList.size());
        } catch (BaseException e){
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * ?????? ??????
     */
    public GetPostsRes getSearchPosts(Long userIdx, String searchWord, String sortType, Long cursorIdx, Integer cursorPrice, Long cursorPopularNum, Pageable page) throws BaseException {
        try{
            User user =this.userRepository.findById(userIdx).orElse(null);
            if (user != null) {
                SearchHistory searchHistory = SearchHistory.builder()
                        .user(user)
                        .searchWord(searchWord)
                        .build();
                this.searchHistoryRepository.save(searchHistory);
            }
            return cursorIdx == null?
                    this.getSearchFeeds(searchWord, user, sortType, page):
                    this.getSearchFeedsWithCursor(searchWord, user, sortType, cursorIdx, cursorPrice, cursorPopularNum, page);
        } catch (BaseException e){
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * ?????? ?????? ??????
     */
    public GetPostsRes getPostsByTag(Long userIdx, String searchTag, String sortType, Long cursorIdx, Integer cursorPrice, Long cursorPopularNum, Pageable page) throws BaseException {
        try{
            Tag tag = this.tagRepository.findByTagName(searchTag)
                    .orElseThrow(() -> new BaseException(INVALID_TAG));
            User user = this.userRepository.findById(userIdx).orElse(null);

            return cursorIdx == null?
                    this.getResByTag(tag, user, sortType, page):
                    this.getResByTagWithCursor(tag, user, sortType, cursorIdx, cursorPrice, cursorPopularNum, page);
        } catch (BaseException e){
            throw e;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * ???????????? ?????? ??????
     */
    private List<GetLikePostRes> getFeeds(User user, Pageable page) throws BaseException {
        return this.postLikeRepository.findByUserAndStatusOrderByLastModifiedDateDesc(user, ACTIVE_STATUS, page).stream()
                .map(postLike -> new GetLikePostRes(postLike.getPost()))
                .collect(Collectors.toList());
    }

    /**
     * ???????????? ?????? ?????? ??????
     */
    private List<GetLikePostRes> getFeedsWithCursor(User user, LocalDateTime lastModifiedDate, Pageable page) throws BaseException {
        return this.postLikeRepository.findByUserAndStatusAndLastModifiedDateLessThanOrderByLastModifiedDateDesc(user, ACTIVE_STATUS, lastModifiedDate, page).stream()
                .map(postLike -> new GetLikePostRes(postLike.getPost()))
                .collect(Collectors.toList());
    }

    /**
     * ???????????? ?????? ??????
     */
    private List<GetPostRes> getFeeds(Store store, User user, Pageable page) throws BaseException {
        return this.postRepository.findByStoreAndStatusOrderByPostIdxDesc(store, ACTIVE_STATUS, page).stream()
                .map(getFeedFunction(user))
                .collect(Collectors.toList());
    }

    /**
     * ???????????? ?????? ?????? ??????
     */
    private List<GetPostRes> getFeedsWithCursor(Store store, User user, Long cursorIdx, Pageable page) throws BaseException {
        this.postRepository.findById(cursorIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));
        return this.postRepository.findByStoreAndStatusAndPostIdxLessThanOrderByPostIdxDesc(store, ACTIVE_STATUS, cursorIdx, page).stream()
                .map(getFeedFunction(user))
                .collect(Collectors.toList());
    }

    /**
     * ???????????? ?????? ??????
     */
    private GetPostsRes getSearchFeeds(String searchWord, User user, String sortType, Pageable page) throws BaseException {
        List<GetPostRes> list;
        Integer lastPopularNum = null;
        Integer lastPrice = null;
        boolean hasNext;
        switch (sortType) {
            case NEW_SORT_TYPE:
                list = this.postRepository.findByDessertDessertNameContainingAndStatusOrderByPostIdxDesc(searchWord, ACTIVE_STATUS, page).stream()
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
                hasNext = hasNext(searchWord, getLastIdxOfList(list));
                break;
            case POPULAR_SORT_TYPE:
                list = this.postRepository.getPopularSortSearchPosts(searchWord, page).stream()
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
                lastPopularNum = getLastPopularNumOfList(list);
                hasNext = hasNextByPopular(searchWord, getLastIdxOfList(list), lastPopularNum);
                break;
            case LOW_PRICE_SORT_TYPE:
                list = this.postRepository.findByDessertDessertNameContainingAndStatusOrderByDessertDessertPriceAscPostIdxDesc(searchWord, ACTIVE_STATUS, page).stream()
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
                lastPrice = getLastPriceOfList(list);
                hasNext = hasNextByPrice(searchWord, getLastIdxOfList(list), lastPrice);
                break;
            default:
                throw new BaseException(INVALID_SORT_TYPE);
        }
        return new GetPostsRes(list, getLastIdxOfList(list),lastPrice,lastPopularNum, hasNext, list.size());
    }

    /**
     * ???????????? ?????? ?????? ??????
     */
    private GetPostsRes getSearchFeedsWithCursor(String searchWord, User user, String sortType, Long cursorIdx, Integer cursorPrice, Long cursorPopularNum, Pageable page) throws BaseException {
        this.postRepository.findById(cursorIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));
        List<GetPostRes> list;
        Integer lastPopularNum = null;
        Integer lastPrice = null;
        boolean hasNext;
        switch (sortType) {
            case NEW_SORT_TYPE:
                list = this.postRepository.findByDessertDessertNameContainingAndStatusAndPostIdxLessThanOrderByPostIdxDesc(searchWord, ACTIVE_STATUS, cursorIdx, page).stream()
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
                hasNext = hasNext(searchWord, getLastIdxOfList(list));
                break;
            case POPULAR_SORT_TYPE:
                list = this.postRepository.getPopularSortSearchPostsWithCursor(searchWord, cursorIdx, cursorPopularNum, page).stream()
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
                lastPopularNum = getLastPopularNumOfList(list);
                hasNext = hasNextByPopular(searchWord, getLastIdxOfList(list), lastPopularNum);
                break;
            case LOW_PRICE_SORT_TYPE:
                list = this.postRepository.getLowPriceSortSearchPostsWithCursor(searchWord, cursorIdx, cursorPrice, page).stream()
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
                lastPrice = getLastPriceOfList(list);
                hasNext = hasNextByPrice(searchWord, getLastIdxOfList(list), lastPrice);
                break;
            default:
                throw new BaseException(INVALID_SORT_TYPE);
        }
        return new GetPostsRes(list, getLastIdxOfList(list),lastPrice,lastPopularNum, hasNext, list.size());
    }

    /**
     * ????????? ?????? ??????
     */
    private GetPostsRes getResByTag(Tag tag, User user, String sortType, Pageable page) throws BaseException {
        List<GetPostRes> list;
        Integer lastPopularNum = null;
        Integer lastPrice = null;
        boolean hasNext;
        switch (sortType) {
            case NEW_SORT_TYPE:
                list = this.postTagRepository.findByTagAndStatusAndPostStatusOrderByPostPostIdxDesc(tag, ACTIVE_STATUS, ACTIVE_STATUS, page).stream()
                        .map(PostTag::getPost)
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
                hasNext = hasNext(tag, getLastIdxOfList(list));
                break;
            case POPULAR_SORT_TYPE:
                list = this.postTagRepository.getPopularSortTagPosts(tag, page).stream()
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
                lastPopularNum = getLastPopularNumOfList(list);
                hasNext = hasNextByPopular(tag, getLastIdxOfList(list), lastPopularNum);
                break;
            case LOW_PRICE_SORT_TYPE:
                list = this.postTagRepository.getLowPriceSortTagPosts(tag, page).stream()
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
                lastPrice = getLastPriceOfList(list);
                hasNext = hasNextByPrice(tag, getLastIdxOfList(list), lastPrice);
                break;
            default:
                throw new BaseException(INVALID_SORT_TYPE);
        }
        return new GetPostsRes(list, getLastIdxOfList(list),lastPrice,lastPopularNum, hasNext, list.size());
    }

    /**
     * ????????? ?????? ?????? ??????
     */
    private GetPostsRes getResByTagWithCursor(Tag tag, User user, String sortType, Long cursorIdx, Integer cursorPrice, Long cursorPopularNum, Pageable page) throws BaseException {
        this.postRepository.findById(cursorIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));
        List<GetPostRes> list;
        Integer lastPopularNum = null;
        Integer lastPrice = null;
        boolean hasNext;
        switch (sortType) {
            case NEW_SORT_TYPE:
                list = this.postTagRepository.findByTagAndStatusAndPostStatusAndPostPostIdxLessThanOrderByPostPostIdxDesc(tag, ACTIVE_STATUS, ACTIVE_STATUS, cursorIdx, page).stream()
                        .map(PostTag::getPost)
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
                hasNext = hasNext(tag, getLastIdxOfList(list));
                break;
            case POPULAR_SORT_TYPE:
                list = this.postTagRepository.getPopularSortTagPostsWithCursor(tag, cursorIdx, cursorPopularNum, page).stream()
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
                lastPopularNum = getLastPopularNumOfList(list);
                hasNext = hasNextByPopular(tag, getLastIdxOfList(list), lastPopularNum);
                break;
            case LOW_PRICE_SORT_TYPE:
                list =  this.postTagRepository.getLowPriceSortTagPostsWithCursor(tag, cursorIdx, cursorPrice, page).stream()
                        .map(getFeedFunction(user))
                        .collect(Collectors.toList());
                lastPrice = getLastPriceOfList(list);
                hasNext = hasNextByPrice(tag, getLastIdxOfList(list), lastPrice);
                break;
            default:
                throw new BaseException(INVALID_SORT_TYPE);
        }
        return new GetPostsRes(list, getLastIdxOfList(list),lastPrice, lastPopularNum, hasNext, list.size());
    }

    /**
     * Feed ?????????
     */
    private Function<Post, GetPostRes> getFeedFunction(User user) {
        return post -> new GetPostRes(
                post,
                this.postLikeRepository.existsByPostAndUserAndStatus(post, user, ACTIVE_STATUS));
    }

    /**
     * @return ?????? ????????? ????????? ?????? idx
     */
    private Long getLastIdxOfList(List<GetPostRes> postList) {
        return postList.isEmpty() ?
                null : postList.get(postList.size() - 1).getPostIdx();
    }

    /**
     * @return ?????? ????????? ????????? ?????? ??????
     */
    private Integer getLastPriceOfList(List<GetPostRes> postList) {
        return postList.isEmpty() ?
                null : postList.get(postList.size() - 1).getDessertPrice();
    }

    /**
     * @return ?????? ????????? ????????? ?????? ?????????
     */
    private Integer getLastPopularNumOfList(List<GetPostRes> postList) {
        return postList.isEmpty() ?
                null : this.postHistoryRepository.countByPostPostIdxEquals(postList.get(postList.size()-1).getPostIdx());
    }

    /**
     * @return ?????? ????????? ????????? lastModifiedDate
     */
    private LocalDateTime getLastDateOfList(List<GetLikePostRes> postList, User user) {
        return postList.isEmpty() ?
                null : this.postLikeRepository.findByPostAndUser(
                        this.postRepository.findById(postList.get(postList.size()-1).getPostIdx()).get(), user).getLastModifiedDate();
    }

    /**
     * ????????????
     * @return ????????? ????????? ?????? ?????? ??????
     */
    private boolean hasNext(User user, LocalDateTime lastDate) {
        return lastDate != null && this.postLikeRepository.existsByUserAndStatusAndLastModifiedDateLessThan(user, ACTIVE_STATUS, lastDate);
    }

    /**
     * ????????????
     * @return ????????? ????????? ?????? ?????? ??????
     */
    private boolean hasNext(Store store, Long lastIdx) {
        return lastIdx != null && this.postRepository.existsByStoreAndStatusAndPostIdxLessThan(store, ACTIVE_STATUS, lastIdx);
    }

    /**
     * ???????????? ?????????
     * @return ????????? ????????? ?????? ?????? ??????
     */
    private boolean hasNext(String searchWord, Long lastIdx) {
        return lastIdx != null && this.postRepository.existsByDessertDessertNameContainingAndStatusAndPostIdxLessThan(searchWord, ACTIVE_STATUS, lastIdx);
    }

    /**
     * ???????????? ?????????
     * @return ????????? ????????? ?????? ?????? ??????
     */
    private boolean hasNextByPopular(String searchWord, Long lastIdx, int lastPopularNum) {
        return lastIdx != null && this.postRepository.existNextByPopularAndPostIdx(searchWord, lastIdx, (long) lastPopularNum);
    }

    /**
     * ???????????? ???????????????
     * @return ????????? ????????? ?????? ?????? ??????
     */
    private boolean hasNextByPrice(String searchWord, Long lastIdx, int lastPrice) {
        return lastIdx != null && this.postRepository.existNextByPriceAndPostIdx(searchWord, lastIdx, lastPrice);
    }

    /**
     * ????????? ?????????
     * @return ????????? ????????? ?????? ?????? ??????
     */
    private boolean hasNext(Tag tag, Long lastIdx) {
        return lastIdx != null && this.postTagRepository.existsByTagAndPostPostIdxLessThan(tag, lastIdx);
    }

    /**
     * ????????? ???????????????
     * @return ????????? ????????? ?????? ?????? ??????
     */
    private boolean hasNextByPrice(Tag tag, Long lastIdx, int lastPrice) {
        return lastIdx != null && this.postTagRepository.existNextByPriceAndPostIdx(tag, lastIdx, lastPrice);
    }

    /**
     * ????????? ?????????
     * @return ????????? ????????? ?????? ?????? ??????
     */
    private boolean hasNextByPopular(Tag tag, Long lastIdx, int lastPopularNum) {
        return lastIdx != null && this.postTagRepository.existNextByPopularAndPostIdx(tag, lastIdx, (long) lastPopularNum);
    }
}
