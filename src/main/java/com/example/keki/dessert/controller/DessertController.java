package com.example.keki.dessert.controller;

import com.example.keki.dessert.service.DessertService;
import com.example.keki.user.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/desserts")
public class DessertController {
    private DessertService dessertService;

    public DessertController(DessertService dessertService){
        this.dessertService = dessertService;
    }

}
