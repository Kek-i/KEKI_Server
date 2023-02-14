package com.codepatissier.keki.history.repository;

import com.codepatissier.keki.history.entity.PostHistory;
import com.codepatissier.keki.user.entity.User;

import java.util.List;

public interface PostHistoryCustom {
    List<PostHistory> getRecentPosts(User user);
}
