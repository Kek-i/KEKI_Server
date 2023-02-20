package com.codepatissier.keki.common.entityListener;

import com.codepatissier.keki.calendar.repository.Calendar.CalendarRepository;
import com.codepatissier.keki.common.BeanUtils;
import com.codepatissier.keki.common.Role;
import com.codepatissier.keki.history.repository.PostHistoryRepository;
import com.codepatissier.keki.history.repository.SearchHistoryRepository;
import com.codepatissier.keki.post.repository.PostLikeRepository;
import com.codepatissier.keki.store.repository.StoreRepository;
import com.codepatissier.keki.user.entity.User;

import javax.persistence.PreRemove;

public class UserEntityListener {

    @PreRemove
    public void onUpdate(User user){
        if(Role.getRoleByName(user.getRole())==Role.CUSTOMER){
            PostLikeRepository postLikeRepository = BeanUtils.getBean(PostLikeRepository.class);
            postLikeRepository.deleteByUser(user);
            PostHistoryRepository postHistoryRepository = BeanUtils.getBean(PostHistoryRepository.class);
            postHistoryRepository.deleteByUser(user);
            SearchHistoryRepository searchHistoryRepository = BeanUtils.getBean(SearchHistoryRepository.class);
            searchHistoryRepository.deleteByUser(user);
            CalendarRepository calendarRepository = BeanUtils.getBean(CalendarRepository.class);
            calendarRepository.deleteByUser(user);
        } else if(Role.getRoleByName(user.getRole())==Role.STORE){
            StoreRepository storeRepository = BeanUtils.getBean(StoreRepository.class);
            storeRepository.deleteByUser(user);
        }
        user.signout();
    }
}
