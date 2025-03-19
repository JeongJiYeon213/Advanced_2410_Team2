package org.zerock.b02.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
	private Long productId;

	@Size(min = 1, max = 12)
	private String productCode;

	@Size(min = 1, max = 100)
	private String productName;
	@Size(min = 1, max = 50)
	private String productType;
	@NotNull(message = "Weight cannot be null")
	private Long productWeight;
	@Size(min = 1, max = 50)
	private String productSize;
	private LocalDateTime regDate;
	private LocalDateTime modDate;
}
