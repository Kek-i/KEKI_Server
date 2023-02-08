package com.codepatissier.keki.post.repository;

import com.codepatissier.keki.post.entity.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostCustom {
    List<Post> getPopularSortSearchPosts(String searchWord, Pageable page);
    List<Post> getPopularSortSearchPostsWithCursor(String searchWord, Long cursorIdx, Long cursorHisCnt, Pageable page);
    List<Post> getLowPriceSortSearchPostsWithCursor(String searchWord, Long cursorIdx, int cursorPrice, Pageable page);
    boolean existNextByPopularAndPostIdx(String searchWord, Long lastIdx, Long lastHisCnt);
    boolean existNextByPriceAndPostIdx(String searchWord, Long cursorIdx, int cursorPrice);
}
