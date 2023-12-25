package com.ansim.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class guideController {

    //게시물 목록 보기
    @GetMapping("/guide/map")
    public void getMap(Model model, HttpSession session) {
        model.addAttribute("accessToken", session.getAttribute("accessToken"));
    }

}
