package com.ansim.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class guideController {

    //게시물 목록 보기
    @GetMapping("/guide/map")
    public void getMap(){}

}
