package org.zerock.b02.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b02.dto.ProductDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.service.ProductService;

import javax.validation.Valid;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/mes/product")
public class ProductController {
	private final ProductService productService;

	@GetMapping("/list")
	public void list(PageRequestDTO pageRequestDTO,
	                 Model model,
	                 @RequestParam(value = "selectedProductId", required = false) Long selectedProductId,
	                 @RequestParam(value = "registerMode", required = false) Boolean registerMode,
	                 @RequestParam(value = "modifyMode", required = false) Boolean modifyMode) {
		PageResponseDTO<ProductDTO> responseDTO = productService.list(pageRequestDTO);
		model.addAttribute("responseDTO", responseDTO);
		model.addAttribute("pageRequestDTO", pageRequestDTO);

		if (selectedProductId != null) {
			ProductDTO selectedProduct = productService.readOne(selectedProductId);
			model.addAttribute("selectedProduct", selectedProduct);
		}

		model.addAttribute("registerMode", registerMode != null && registerMode);
		model.addAttribute("modifyMode", modifyMode != null && modifyMode);
	}

	@GetMapping("/register")
	public void registerGET() {
	}



	@PostMapping("/register")
	public String registerPost(@Valid ProductDTO productDTO,
	                           BindingResult bindingResult,
	                           RedirectAttributes redirectAttributes) {
		log.info("product POST register");

		if (bindingResult.hasErrors()) {
			log.info("error");
			redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
			return "redirect:/mes/product/list";
		}

		log.info(productDTO);
		Long productId = productService.register(productDTO);
		redirectAttributes.addFlashAttribute("result", productId);

		return "redirect:/mes/product/list";
	}

	@GetMapping({"/read", "/modify"})
	public String read(@RequestParam("selectedProductId") Long selectedProductId,
	                 PageRequestDTO pageRequestDTO,
	                 Model model) {
		ProductDTO productDTO = productService.readOne(selectedProductId);

		log.info("read: " + productDTO);

		model.addAttribute("selectedProduct", productDTO);
		model.addAttribute("pageRequestDTO", pageRequestDTO);

		return "mes/product/modify";
	}

	@PostMapping("/modify")
	public String modify(@ModelAttribute("productDTO") ProductDTO productDTO,
						 PageRequestDTO pageRequestDTO,
	                     BindingResult bindingResult,
	                     RedirectAttributes redirectAttributes) {

		log.info("product modify POST" + productDTO);
		log.info("Received productSize: " + productDTO.getProductSize());

		if (bindingResult.hasErrors()) {
			log.info("error");

			String link = pageRequestDTO.getLink();
			redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
			redirectAttributes.addAttribute("selectedProductId", productDTO.getProductId());
			return "redirect:/mes/product/list?" + link;
		}

		productService.modify(productDTO);
		redirectAttributes.addFlashAttribute("result", "modified");
		redirectAttributes.addAttribute("selectedProductId", productDTO.getProductId());

		return "redirect:/mes/product/list";
	}
	@PostMapping("/remove")
	public String remove(@RequestParam Long productId, RedirectAttributes redirectAttributes) {
		log.info("remove POST " + productId);
		productService.remove(productId);
		redirectAttributes.addFlashAttribute("result", "removed");
		return "redirect:/mes/product/list";
	}

	@GetMapping("/searchPopup")
	public String showProductSearchPopup(Model model) {
		List<ProductDTO> productList = productService.getAllProducts();
		model.addAttribute("productList", productList);
		return "/mes/product/searchPopup";
	}
}