package com.codepatissier.keki.history.service;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponseStatus;
import com.codepatissier.keki.history.dto.HistorySearchRes;
import com.codepatissier.keki.history.dto.PostSearchRes;
import com.codepatissier.keki.history.dto.SearchRes;
import com.codepatissier.keki.history.entity.PostHistory;
import com.codepatissier.keki.history.repository.PostHistoryRepository;
import com.codepatissier.keki.post.entity.PostImg;
import com.codepatissier.keki.post.service.PostService;
import com.codepatissier.keki.user.entity.User;
import com.codepatissier.keki.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostHistoryService {
    private final PostHistoryRepository postHistoryRepository;
    private final UserRepository userRepository;

    public List<PostSearchRes> recentSearchPost(Long userIdx) throws BaseException {
        try{
            User user = this.userRepository.findById(userIdx)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER_IDX));
            return this.postHistoryRepository.findTop5ByUserOrderByCreatedDateDesc(user).stream()
                    .map(postSearch -> new PostSearchRes(postSearch.getPost().getPostIdx(),
                            this.representPostImgUrl(postSearch.getPost().getImages()))).collect(Collectors.toList());
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    private String representPostImgUrl(List<PostImg> postImgs){
        if(postImgs.isEmpty()) return null;
        else return postImgs.get(0).getImgUrl();
    }

    public HistorySearchRes getSearches(HistorySearchRes searches, Long userIdx) throws BaseException{
        searches.setRecentPostSearches(recentSearchPost(userIdx));
        return searches;
    }
}
