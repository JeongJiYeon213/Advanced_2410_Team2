package org.zerock.b02.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b02.dto.InboundDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.service.InboundService;

import javax.validation.Valid;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/mes")
public class InboundController {
    private final InboundService inboundService;

    @GetMapping("/inbound/list")
    public void list(PageRequestDTO pageRequestDTO,
                     Model model,
                     @RequestParam(value = "selectedInboundId", required = false) Long selectedInboundId,
                     @RequestParam(value = "registerMode", required = false) Boolean registerMode,
                     @RequestParam(value = "modifyMode", required = false) Boolean modifyMode) {

        PageResponseDTO < InboundDTO > responseDTO = inboundService.list(pageRequestDTO);

        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        if (selectedInboundId != null) {
            InboundDTO selectedInbound = inboundService.readOne(selectedInboundId);
            model.addAttribute("selectedInbound", selectedInbound);
        }

        model.addAttribute("registerMode", registerMode != null && registerMode);
        model.addAttribute("modifyMode", modifyMode != null && modifyMode);
    }

    @GetMapping("/inbound/register")
    public void inboundRegisterGet() {

    }


    @PostMapping("/inbound/register")
    public String registerPost(@Valid InboundDTO inboundDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        log.info("inbound Post register.........");

        if (bindingResult.hasErrors()) {
            log.info("has errors.....");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/mes/inbound/register";
        }
        log.info(inboundDTO);

        Long inboundId = inboundService.register(inboundDTO);
        redirectAttributes.addFlashAttribute("result", inboundId);
        return "redirect:/mes/inbound/list";
    }

    @GetMapping({
            "/inbound/read",
            "/inbound/modify"
    })
    public String read(@RequestParam("selectedInboundId") Long selectedInboundId,
                       PageRequestDTO pageRequestDTO, Model model) {
        InboundDTO inboundDTO = inboundService.readOne(selectedInboundId);
        log.info(inboundDTO);
        model.addAttribute("selectedInbound", inboundDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        return "mes/inbound/modify";
    }

    @PostMapping("/inbound/modify")
    public String modify(@Valid InboundDTO inboundDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         PageRequestDTO pageRequestDTO) {
        log.info("inbound Post modify.........");

        if (bindingResult.hasErrors()) {
            log.info("has errors.....");
            String link = pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("inboundId", inboundDTO.getInboundId());
            return "redirect:/mes/inbound/list?" + link;
        }

        inboundService.modify(inboundDTO);
        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("selectedInboundId", inboundDTO.getInboundId());
        return "redirect:/mes/inbound/list";
    }

    @PostMapping("/inbound/remove")
    public String remove(Long inboundId, RedirectAttributes redirectAttributes, PageRequestDTO pageRequestDTO) {
        log.info("remove post..." + inboundId);
        String link = pageRequestDTO.getLink();
        inboundService.remove(inboundId);
        redirectAttributes.addFlashAttribute("result", "removed");
        redirectAttributes.addFlashAttribute("link", link);

        return "redirect:/mes/inbound/list";
    }
}
