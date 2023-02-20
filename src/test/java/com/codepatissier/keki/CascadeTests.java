package com.codepatissier.keki;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.dessert.entity.Dessert;
import com.codepatissier.keki.dessert.repository.DessertRepository;
import com.codepatissier.keki.store.entity.Store;
import com.codepatissier.keki.store.repository.StoreRepository;
import com.codepatissier.keki.user.entity.User;
import com.codepatissier.keki.user.repository.UserRepository;
import com.codepatissier.keki.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.codepatissier.keki.common.Constant.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class CascadeTests {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    DessertRepository dessertRepository;

    @Test
    @DisplayName("판매자 회원탈퇴 시 관련 DB 전체 INACTIVE 처리")
    void signOutStore_AllConnectedDB_INACTIVE() throws BaseException {
        // given
        User user = userRepository.findById(2L).orElseThrow();
        // when
        userService.signout(user.getUserIdx());
        // then
        Store store = storeRepository.findByUser(user).orElseThrow();
        List<Dessert> desserts = dessertRepository.findByStoreAndStatusOrderByDessertIdx(store,ACTIVE_STATUS);
        assertThat(user.getStatus()).isEqualTo(INACTIVE_STATUS);
        assertThat(store.getStatus()).isEqualTo(INACTIVE_STATUS);
        assertThat(desserts.size()).isEqualTo(0);
    }
}
