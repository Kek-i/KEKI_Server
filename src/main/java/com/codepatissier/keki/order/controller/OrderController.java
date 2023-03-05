package com.codepatissier.keki.order.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "Bearer")
@Tag(name = "orders", description = "주문 API")
@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
public class OrderController {
}
