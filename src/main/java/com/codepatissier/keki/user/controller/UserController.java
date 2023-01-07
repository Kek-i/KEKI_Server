package com.codepatissier.keki.user.controller;

import com.codepatissier.keki.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "users", description = "구매자 API")
@RestController
@RequestMapping(value = "/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

}
