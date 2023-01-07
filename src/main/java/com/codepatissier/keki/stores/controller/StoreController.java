package com.codepatissier.keki.stores.controller;

import com.codepatissier.keki.stores.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/stores")
@RequiredArgsConstructor
public class StoreController {
    private StoreService storeService;
}
