package com.codepatissier.keki.post.controller;

import com.codepatissier.keki.post.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "posts", description = "피드 API")
@RestController
@RequestMapping(value = "/posts")
public class PostController {
    private PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

}
