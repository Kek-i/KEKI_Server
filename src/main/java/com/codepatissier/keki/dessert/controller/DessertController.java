package com.codepatissier.keki.dessert.controller;

import com.codepatissier.keki.dessert.service.DessertService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "desserts", description = "상품 API")
@RestController
@RequestMapping(value = "/desserts")
public class DessertController {
    private DessertService dessertService;

    public DessertController(DessertService dessertService){
        this.dessertService = dessertService;
    }

}
