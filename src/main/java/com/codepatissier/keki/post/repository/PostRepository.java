package com.codepatissier.keki.post.repository;

import com.codepatissier.keki.dessert.entity.Dessert;
import com.codepatissier.keki.post.entity.Post;
import com.codepatissier.keki.store.entity.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustom {
    // 스토어별 피드
    List<Post> findByStoreAndStatusOrderByPostIdxDesc(Store store, String status, Pageable page);
    List<Post> findByStoreAndStatusAndPostIdxLessThanOrderByPostIdxDesc(Store store, String status, Long postIdx, Pageable page);
    // 검색어별 피드
    List<Post> findByDessertDessertNameContainingAndStatusOrderByPostIdxDesc(String word, String status, Pageable page);
    List<Post> findByDessertDessertNameContainingAndStatusAndPostIdxLessThanOrderByPostIdxDesc(String word, String status, Long postIdx, Pageable page);
    List<Post> findByDessertDessertNameContainingAndStatusOrderByDessertDessertPriceAscPostIdxDesc(String word, String status, Pageable page);

    boolean existsByStoreAndStatusAndPostIdxLessThan(Store store, String status, Long postIdx);
    boolean existsByDessertDessertNameContainingAndStatusAndPostIdxLessThan(String word, String status, Long postIdx);
    List<Post> findTop5ByDessertAndStatusOrderByPostIdxDesc(Dessert dessert, String activeStatus);

    void deleteByDessert(Dessert dessert);
}