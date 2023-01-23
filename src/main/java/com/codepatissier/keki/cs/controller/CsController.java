package com.codepatissier.keki.cs.controller;

import com.codepatissier.keki.cs.service.CsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "Bearer")
@Tag(name = "cs", description = "CS API")
@RestController
@RequestMapping(value = "/cs")
@RequiredArgsConstructor
public class CsController {
    private CsService csService;

}
