package com.codepatissier.keki.post.repository;

import com.codepatissier.keki.common.Tag.Tag;
import com.codepatissier.keki.post.entity.Post;
import com.codepatissier.keki.post.entity.PostTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Long>, PostTagCustom {
    List<PostTag> findByTagAndStatusAndPostStatusOrderByPostPostIdxDesc(Tag tag, String status, String postStatus, Pageable page);
    List<PostTag> findByTagAndStatusAndPostStatusAndPostPostIdxLessThanOrderByPostPostIdxDesc(Tag tag, String status, String postStatus, Long postIdx, Pageable page);
    PostTag findByPostAndTag(Post post, Tag tag);
    boolean existsByTagAndPostPostIdxLessThan(Tag tag, Long postIdx);
}
