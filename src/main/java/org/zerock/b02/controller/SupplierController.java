package org.zerock.b02.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.dto.SupplierDTO;
import org.zerock.b02.service.SupplierService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/mes/supplier")
@Log4j2
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO,
                     Model model,
                     @RequestParam(value = "selectedSupplierId", required = false) String selectedSupplierId,
                     @RequestParam(value = "registerMode", required = false) Boolean registerMode,
                     @RequestParam(value = "modifyMode", required = false) Boolean modifyMode){

        PageResponseDTO<SupplierDTO> responseDTO = supplierService.list(pageRequestDTO);

        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        if(selectedSupplierId != null){
            SupplierDTO selectedSupplier = supplierService.readOne(selectedSupplierId);
            model.addAttribute("selectedSupplier", selectedSupplier);
        }

        model.addAttribute("registerMode", registerMode != null && registerMode);
        model.addAttribute("modifyMode", modifyMode != null && modifyMode);
    }

    @GetMapping("/register")
    public void registerGET(){

    }

    @PostMapping("/register")
    public String registerPost(@Valid SupplierDTO supplierDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("supplier POST register...");

        if (bindingResult.hasErrors()) {
            log.info("has errors...");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/mes/supplier/list";
        }

        log.info(supplierDTO);
        String supplierId = supplierService.register(supplierDTO);
        redirectAttributes.addFlashAttribute("result", supplierId);

        return "redirect:/mes/supplier/list";
    }

    @GetMapping({"/read", "/modify"})
    public String read(@RequestParam("selectedSupplierId") String selectedSupplierId,
                       PageRequestDTO pageRequestDTO, Model model){

        SupplierDTO supplierDTO = supplierService.readOne(selectedSupplierId);

        log.info("read:" + supplierDTO);

        model.addAttribute("selectedSupplier", supplierDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        return "mes/supplier/modify";
    }

    @PostMapping("/modify")
    public String modify(@ModelAttribute("supplierDTO") SupplierDTO supplierDTO,
                         PageRequestDTO pageRequestDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){

        log.info("supplier modify POST" + supplierDTO);
        log.info("Received productSize: " + supplierDTO.getSupplierSize());

        if(bindingResult.hasErrors()){
            log.info("has errors...");

            String link = pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("selectedCustomerId",supplierDTO.getSupplierId());
            return "redirect:/mes/supplier/modify?"+link;
        }

        supplierService.modify(supplierDTO);
        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("supplierId", supplierDTO.getSupplierId());

        return "redirect:/mes/supplier/list";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam String supplierId,
                         RedirectAttributes redirectAttributes){

        log.info("romove post.." + supplierId);
        supplierService.remove(supplierId);
        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/mes/supplier/list";
    }


    @GetMapping("/searchPopup")
    public String showSupplierSearchPopup(Model model) {
        List<SupplierDTO> supplierList = supplierService.getAllSuppliers();
        model.addAttribute("supplierList", supplierList);
        return "/mes/supplier/searchPopup";
    }
}
