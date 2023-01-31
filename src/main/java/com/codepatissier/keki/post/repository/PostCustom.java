package com.codepatissier.keki.post.repository;

import com.codepatissier.keki.post.entity.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostCustom {
    List<Post> getPopularSortSearchPosts(String searchWord, Pageable page);
    List<Post> getPopularSortSearchPostsWithCursor(String searchWord, Long cursorIdx, Pageable page);
}
