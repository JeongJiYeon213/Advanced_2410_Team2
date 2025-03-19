package org.zerock.b02.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.b02.dto.PageRequestDTO;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/mes")
public class DashboardController {

    @GetMapping("/dashboard")
    public void dashboard(Model model, PageRequestDTO pageRequestDTO){
    }
}
