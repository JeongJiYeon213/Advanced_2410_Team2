package org.zerock.b02.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;  // 제품 고유 ID

	@Column(length = 500, nullable = false, unique = true)
	private String productCode;  // 제품 코드

	@Column(length = 500, nullable = false)
	private String productName;  // 제품 이름

	@Column(length = 500, nullable = false)
	private String productType;  // 제품 타입 (예: 철강, 알루미늄 등)

	@Column(nullable = false)
	private Long productWeight;  // 제품 단위 무게 (kg 등)

	@Column(nullable = false)
	private String productSize;  // 제품 크기 (예: 2m x 3m 등)

	public void change(String productCode,
	                   String productName,
	                   String productType,
					   Long productWeight,
	                   String productSize) {
		this.productCode = productCode;
		this.productName = productName;
		this.productType = productType;
		this.productWeight = productWeight;
		this.productSize = productSize;

	}
}
