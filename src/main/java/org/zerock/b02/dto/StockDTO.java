package org.zerock.b02.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockDTO {
	private Long stockId;
	private Long productId;
	private String productCode;

	@NotNull(message = "Current stock cannot be null")
	private Long currentStock;

	private LocalDateTime regDate;
	private LocalDateTime modDate;
}
