package com.codepatissier.keki;

import com.codepatissier.keki.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
@RequiredArgsConstructor
public class TestController {
    @ResponseBody
    @GetMapping("/logs")
    public BaseResponse<String> testAPI() {
        return new BaseResponse<>("성공 해써용");
    }
}