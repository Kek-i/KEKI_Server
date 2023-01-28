package com.codepatissier.keki.store.repository;

import com.codepatissier.keki.store.entity.Store;
import com.codepatissier.keki.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByUser(User user); // TODO PostService에서 findByUserAndStatus로 변경 후 삭제
    Optional<Store> findByUserAndStatus(User user, String activeStatus);
    Optional<Store> findByStoreIdxAndStatus(Long storeIdx, String activeStatus);
}