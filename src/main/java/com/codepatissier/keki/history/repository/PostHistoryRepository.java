package com.codepatissier.keki.history.repository;

import com.codepatissier.keki.history.entity.PostHistory;
import com.codepatissier.keki.post.entity.Post;
import com.codepatissier.keki.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostHistoryRepository extends JpaRepository<PostHistory, Long>, PostHistoryCustom{
    int countByPostPostIdxEquals(Long postIdx);
    void deleteByPost(Post post);
    void deleteByUser(User user);

    List<PostHistory> findByUserAndStatus(User user, String status);
}