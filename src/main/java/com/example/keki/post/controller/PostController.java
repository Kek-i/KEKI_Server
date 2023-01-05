package com.example.keki.post.controller;

import com.example.keki.post.service.PostService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/posts")
public class PostController {
    private PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

}
