package com.codepatissier.keki.stores.controller;

import com.codepatissier.keki.stores.service.StoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "stores", description = "판매자 API")
@RestController
@RequestMapping(value = "/stores")
@RequiredArgsConstructor
public class StoreController {
    private StoreService storeService;
}
