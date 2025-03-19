package org.zerock.b02.service;

import org.zerock.b02.domain.Stock;
import org.zerock.b02.dto.ProductDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;

import java.util.List;

public interface ProductService {
	Long register(ProductDTO productDTO);

	ProductDTO readOne(Long productId);

	void modify(ProductDTO productDTO);

	void remove(Long productId);

	PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO);

	List<ProductDTO> getAllProducts();

	Stock getStockByProductCode(String productCode);

}
