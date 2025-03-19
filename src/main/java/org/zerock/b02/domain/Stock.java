package org.zerock.b02.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "product")
public class Stock extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long stockId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(nullable = false)
	private Long currentStock;

	@Column(nullable = false)
	private String productCode;

	public void change(Long currentStock) {
		if (currentStock == null || currentStock < 0) {
			throw new IllegalArgumentException("재고는 0 이상이어야 합니다.");
		}
		this.currentStock = currentStock;
	}
}
