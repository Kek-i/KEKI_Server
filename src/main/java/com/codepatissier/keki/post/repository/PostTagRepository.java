package com.codepatissier.keki.post.repository;

import com.codepatissier.keki.common.Tag.Tag;
import com.codepatissier.keki.post.entity.PostTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    List<PostTag> findByTagOrderByPostPostIdxDesc(Tag tag, Pageable page);
    List<PostTag> findByTagAndPostPostIdxLessThanOrderByPostPostIdxDesc(Tag tag, Long postIdx, Pageable page);
    boolean existsByTagAndPostPostIdxLessThan(Tag tag, Long postIdx);
}
