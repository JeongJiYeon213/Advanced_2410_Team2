package org.zerock.b02.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b02.dto.AdminDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.service.AdminService;

import javax.validation.Valid;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/mes")
public class AdminController {

    private final AdminService adminService;
    @GetMapping("/adminpage")
    public void adminGet(PageRequestDTO pageRequestDTO, Model model,
                         @RequestParam(value = "selectedAdminBno", required = false) Long selectedAdminBno,
                         @RequestParam(value = "registerMode", required = false) Boolean registerMode,
                         @RequestParam(value = "modifyMode", required = false) Boolean modifyMode
                         ) {
        PageResponseDTO responseDTO = adminService.list(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);

        if(selectedAdminBno != null) {
            AdminDTO selectedAdmin = adminService.readOne(selectedAdminBno);
            model.addAttribute("selectedAdmin", selectedAdmin);
        }

        model.addAttribute("registerMode", registerMode != null && registerMode);
        model.addAttribute("modifyMode", modifyMode != null && modifyMode);
    }

    @GetMapping("/register")
    public void registerGET() {
    }

    @PostMapping("/register")
    public String registerPOST (
            @Valid AdminDTO adminDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        log.info("POST mes register.......");
        log.info(adminDTO);

        if(bindingResult.hasErrors()) {
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            return "redirect:/mes/register";
        }

        Long bno = adminService.register(adminDTO);

        redirectAttributes.addFlashAttribute("result", bno);

        return "redirect:/mes/adminpage";
    }

    @GetMapping({"/read", "/modify"})
    public String read(@RequestParam("selectedAdminId") Long selectedAdminId,
                       PageRequestDTO pageRequestDTO,
                       Model model) {
        AdminDTO adminDTO = adminService.readOne(selectedAdminId);

        log.info("read: " + adminDTO);

        model.addAttribute("selectedAdmin", adminDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        return "mes/modify";  // 이 부분은 수정 폼을 보여주는 view로 변경
    }

    @PostMapping("/modify")
    public String modify(@ModelAttribute("adminDTO") AdminDTO adminDTO,
                         PageRequestDTO pageRequestDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        log.info("admin modify POST: " + adminDTO);

        if (bindingResult.hasErrors()) {
            log.info("Validation errors occurred");

            String link = pageRequestDTO.getLink();

            if (link == null || link.isEmpty()) {
                link = "page=1";
            }

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            redirectAttributes.addAttribute("bno", adminDTO.getBno());

            return "redirect:/mes/adminpage";
        }

        adminService.modify(adminDTO);

        redirectAttributes.addFlashAttribute("result", "modified");

        redirectAttributes.addAttribute("bno", adminDTO.getBno());

        return "redirect:/mes/adminpage";
    }

    @PostMapping("/remove")
    public String remove(Long bno, RedirectAttributes rttr) {
        adminService.remove(bno);

        rttr.addFlashAttribute("result", "removed");

        return "redirect:/mes/adminpage";  // 삭제 이후 page번호 1번으로 이동;
    }
}
