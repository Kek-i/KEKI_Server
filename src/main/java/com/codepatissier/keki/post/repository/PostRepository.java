package com.codepatissier.keki.post.repository;

import com.codepatissier.keki.post.entity.Post;
import com.codepatissier.keki.store.entity.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByStoreOrderByPostIdxDesc(Store store, Pageable page);
    List<Post> findByStoreAndPostIdxLessThanOrderByPostIdxDesc(Store store, Long postIdx, Pageable page);
    boolean existsByPostIdxLessThan(Long postIdx);
}
