package com.codepatissier.keki.post.repository;

import com.codepatissier.keki.common.Tag.Tag;
import com.codepatissier.keki.post.entity.PostTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Long>, PostTagCustom {
    List<PostTag> findByTagAndPostStatusOrderByPostPostIdxDesc(Tag tag, String status, Pageable page);
    List<PostTag> findByTagAndPostStatusAndPostPostIdxLessThanOrderByPostPostIdxDesc(Tag tag, String status, Long postIdx, Pageable page);
    boolean existsByTagAndPostPostIdxLessThan(Tag tag, Long postIdx);
}
