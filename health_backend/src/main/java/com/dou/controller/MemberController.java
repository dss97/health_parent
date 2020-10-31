package com.dou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.service.MemberService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Reference
    private MemberService memberService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return  memberService.findPage(queryPageBean);
    }
}
