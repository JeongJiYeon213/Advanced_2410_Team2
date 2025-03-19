package org.zerock.b02.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b02.dto.ProductDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ProductServiceTests {
	@Autowired
	private ProductService productService;

	@Test
	public void testRegister() {
		log.info(productService.getClass().getName());

		List<String> productNames = Arrays.asList("바", "플레이트", "와이어", "각관", "H형강", "파이프", "채널", "아이빔", "강판", "앵글", "메쉬");
		List<String> productTypes = Arrays.asList("steel", "copper", "aluminum", "iron", "brass", "bronze", "nickel", "titanium");
		List<Long> productWeights = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L);
		List<String> productSizes = Arrays.asList("1m x 1m", "1m x 2m", "2m x 1m", "2m x 2m", "2m x 3m", "3m x 3m", "3m x 4m", "4m x 4m", "4m x 5m", "5m x 5m", "5m x 6m", "6m x 6m", "6m x 7m");

		Random random = new Random();

		IntStream.rangeClosed(1, 110).forEach(i -> {
			String productCode = String.format("P%03d", i);
			String productName = productNames.get(random.nextInt(productNames.size()));
			String productType = productTypes.get(random.nextInt(productTypes.size()));
			Long productWeight = productWeights.get(random.nextInt(productWeights.size()));
			String productSize = productSizes.get(random.nextInt(productSizes.size()));

			ProductDTO productDTO = ProductDTO.builder()
					.productCode(productCode)
					.productName(productName)
					.productType(productType)
					.productWeight(productWeight)
					.productSize(productSize)
					.build();

			Long productId = productService.register(productDTO);

			log.info("Registered Product - productId: " + productId);
		});
	}

	@Test
	public void testReadOne() {

	}

	@Test
	public void testModify() {
		ProductDTO productDTO = ProductDTO.builder()
				.productId(11L)
				.productCode("S002")
				.productName("updated")
				.build();

		productService.modify(productDTO);
	}
	@Test
	public void testRemove() {
		Long productId = 5L;

		productService.remove(productId);
	}

	@Test
	public void testList() {
		PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
				.type("n")
				.keyword("Sample")
				.page(1)
				.size(10)
				.build();

		PageResponseDTO<ProductDTO> responseDTO = productService.list(pageRequestDTO);

		log.info(responseDTO);
	}
}
