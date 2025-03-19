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
import org.zerock.b02.dto.OutboundDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.service.OutboundService;

import javax.validation.Valid;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/mes")
public class OutboundController {
    private final OutboundService outboundService;

    @GetMapping("/outbound/list")
    public void list(PageRequestDTO pageRequestDTO,
                     Model model,
                     @RequestParam(value = "selectedOutboundId", required = false) Long selectedOutboundId,
                     @RequestParam(value = "registerMode", required = false) Boolean registerMode,
                     @RequestParam(value = "modifyMode", required = false) Boolean modifyMode) {

        PageResponseDTO < OutboundDTO > responseDTO = outboundService.list(pageRequestDTO);

        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        if (selectedOutboundId != null) {
            OutboundDTO selectedOutbound = outboundService.readOne(selectedOutboundId);
            model.addAttribute("selectedOutbound", selectedOutbound);
        }

        model.addAttribute("registerMode", registerMode != null && registerMode);
        model.addAttribute("modifyMode", modifyMode != null && modifyMode);
    }

    @GetMapping("/outbound/register")
    public void outboundRegisterGet() {

    }

    @PostMapping("/outbound/register")
    public String registerPost(@Valid OutboundDTO outboundDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        log.info("outbound Post register.........");

        if (bindingResult.hasErrors()) {
            log.info("has errors.....");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/mes/outbound/register";
        }

        log.info(outboundDTO);

        Long outboundId = outboundService.register(outboundDTO);
        redirectAttributes.addFlashAttribute("result", outboundId);
        return "redirect:/mes/outbound/list";
    }

    @GetMapping({
            "/outbound/read",
            "/outbound/modify"
    })
    public String read(@RequestParam("selectedOutboundId") Long selectedOutboundId,
                       PageRequestDTO pageRequestDTO, Model model) {
        OutboundDTO outboundDTO = outboundService.readOne(selectedOutboundId);
        log.info(outboundDTO);
        model.addAttribute("selectedOutbound", outboundDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        return "mes/outbound/modify";
    }

    @PostMapping("/outbound/modify")
    public String modify(@Valid OutboundDTO outboundDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         PageRequestDTO pageRequestDTO) {
        log.info("outbound Post modify.........");

        if (bindingResult.hasErrors()) {
            log.info("has errors.....");
            String link = pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("outboundId", outboundDTO.getOutboundId());
            return "redirect:/mes/outbound/list?" + link;
        }

        outboundService.modify(outboundDTO);
        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("outboundId", outboundDTO.getOutboundId());
        return "redirect:/mes/outbound/list";
    }

    @PostMapping("/outbound/remove")
    public String remove(Long outboundId, RedirectAttributes redirectAttributes, PageRequestDTO pageRequestDTO) {
        log.info("remove post..." + outboundId);
        String link = pageRequestDTO.getLink();
        outboundService.remove(outboundId);
        redirectAttributes.addFlashAttribute("result", "removed");
        redirectAttributes.addFlashAttribute("link", link);


        return "redirect:/mes/outbound/list";
    }
}