package com.yanxin.admin.controller;

import com.yanxin.admin.feign.NacosFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program spring-cloud-example
 * @description:
 * @author: LiuYanXin
 * @create: 2020-09-02 15:09
 */
@RestController
public class AdminController {

    @Autowired
    private NacosFeignService nacosFeignService;

    @GetMapping(value = "/test/hi")
    public String test() {

        return nacosFeignService.test("Hi Feign");
    }
}
