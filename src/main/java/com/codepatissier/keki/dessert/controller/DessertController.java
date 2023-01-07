package com.codepatissier.keki.dessert.controller;

import com.codepatissier.keki.dessert.service.DessertService;
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
