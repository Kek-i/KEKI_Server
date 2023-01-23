package com.codepatissier.keki.post.repository;

import com.codepatissier.keki.post.entity.PostImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImgRepository extends JpaRepository<PostImg, Long> {
}
