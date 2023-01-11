package com.codepatissier.keki.history.service;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponseStatus;
import com.codepatissier.keki.history.dto.SearchRes;
import com.codepatissier.keki.history.repository.SearchHistoryRepository;
import com.codepatissier.keki.user.entity.User;
import com.codepatissier.keki.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.codepatissier.keki.common.Constant.ACTIVE_STATUS;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final UserRepository userRepository;
    private final SearchHistoryRepository searchHistoryRepository;

    public List<SearchRes> recentSearch(Long userIdx) throws BaseException{
        try{
            User user = this.userRepository.findById(userIdx)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER_IDX));

            return this.searchHistoryRepository.findTop5ByUserAndStatusOrderByCreatedDateDesc(user,ACTIVE_STATUS).stream()
                    .map(searches -> new SearchRes(searches.getSearchWord())).collect(Collectors.toList());
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }

}
