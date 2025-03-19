package org.zerock.b02.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b02.dto.StockDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.service.ProductService;
import org.zerock.b02.service.StockService;

import javax.validation.Valid;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/mes/stock")
public class StockController {

	private final StockService stockService;
	private final ProductService productService;

	@GetMapping("/list")
	public void list(PageRequestDTO pageRequestDTO,
	                 Model model,
	                 @RequestParam(value = "selectedStockId", required = false) Long selectedStockId,
	                 @RequestParam(value = "registerMode", required = false) Boolean registerMode,
	                 @RequestParam(value = "modifyMode", required = false) Boolean modifyMode) {

		PageResponseDTO<StockDTO> responseDTO = stockService.list(pageRequestDTO);
		model.addAttribute("responseDTO", responseDTO);
		model.addAttribute("pageRequestDTO", pageRequestDTO);

		if (selectedStockId != null) {
			StockDTO selectedStock = stockService.readOne(selectedStockId);
			model.addAttribute("selectedStock", selectedStock);
		}


		model.addAttribute("productList", productService.getAllProducts());

		model.addAttribute("registerMode", registerMode != null && registerMode);
		model.addAttribute("modifyMode", modifyMode != null && modifyMode);
	}



	@GetMapping("/register")
	public void registerGET() {
	}

	@PostMapping("/register")
	public String registerPost(@Valid StockDTO stockDTO,
	                           BindingResult bindingResult,
	                           RedirectAttributes redirectAttributes) {
		log.info("Stock POST register");

		if (bindingResult.hasErrors()) {
			log.info("Validation error");
			redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
			return "redirect:/mes/stock/list";
		}

		log.info(stockDTO);

		return "redirect:/mes/stock/list";
	}

	@GetMapping({"/read", "/modify"})
	public String read(@RequestParam("selectedStockId") Long selectedStockId,
	                   PageRequestDTO pageRequestDTO,
	                   Model model) {
		StockDTO stockDTO = stockService.readOne(selectedStockId);

		log.info("read: " + stockDTO);

		model.addAttribute("selectedStock", stockDTO);
		model.addAttribute("pageRequestDTO", pageRequestDTO);

		return "mes/stock/modify";
	}

	@PostMapping("/modify")
	public String modify(@ModelAttribute("stockDTO") StockDTO stockDTO,
	                     PageRequestDTO pageRequestDTO,
	                     BindingResult bindingResult,
	                     RedirectAttributes redirectAttributes) {

		log.info("Stock modify POST: " + stockDTO);

		if (bindingResult.hasErrors()) {
			log.info("Validation error");

			String link = pageRequestDTO.getLink();
			redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
			redirectAttributes.addAttribute("selectedStockId", stockDTO.getStockId());
			return "redirect:/mes/stock/list?" + link;
		}

		stockService.modify(stockDTO);
		redirectAttributes.addFlashAttribute("result", "modified");
		redirectAttributes.addAttribute("selectedStockId", stockDTO.getStockId());

		return "redirect:/mes/stock/list";
	}

	@PostMapping("/remove")
	public String remove(@RequestParam Long stockId, RedirectAttributes redirectAttributes) {
		log.info("remove POST " + stockId);
		stockService.remove(stockId);
		redirectAttributes.addFlashAttribute("result", "removed");
		return "redirect:/mes/stock/list";
	}
}