package org.zerock.b02.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zerock.b02.domain.Admin;
import org.zerock.b02.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@SessionAttributes("admin") // 세션에 "admin" 객체를 저장
@RequestMapping("/mes")
public class LoginController {

    private final AdminService adminService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("adminId") Long adminId,
                        @RequestParam("adminPassword") int adminPassword,
                        HttpSession session,
                        Model model) {
        Admin admin = adminService.login(adminId, adminPassword);

        if (admin != null) {
            session.setAttribute("adminName", admin.getAdminName());
            session.setAttribute("position", admin.getPosition());
            model.addAttribute("admin", admin); // 세션에 admin 객체 저장
            session.setAttribute("adminId", adminId);
            return "redirect:/mes/dashboard"; // 로그인 성공 시 대시보드로 리다이렉트
        } else {
            model.addAttribute("error", "아이디나 비밀번호가 잘못되었습니다.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpSession session) {
        session.invalidate();
        return "redirect:/mes/login";
    }
}