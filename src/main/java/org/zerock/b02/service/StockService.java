package org.zerock.b02.service;

import org.zerock.b02.dto.StockDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;

import java.util.List;

public interface StockService {

	StockDTO readOne(Long stockId);

	void modify(StockDTO stockDTO);

	void remove(Long stockId);

	PageResponseDTO<StockDTO> list(PageRequestDTO pageRequestDTO);

	List<StockDTO> getAllStocks();
}