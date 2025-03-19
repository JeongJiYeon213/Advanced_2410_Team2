package org.zerock.b02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.zerock.b02.domain.Product;
import org.zerock.b02.domain.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.b02.repository.search.StockSearch;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long>, StockSearch {
	@Query("SELECT s FROM Stock s WHERE s.product.productName = :productName")
	List<Stock> findByProductName(@Param("productName") String productName);

	@Query("SELECT s FROM Stock s WHERE s.product.productName LIKE CONCAT('%', :keyword, '%')")
	Page<Stock> findKeyword(@Param("keyword") String keyword, Pageable pageable);

	boolean existsByProduct(Product product);

	Optional<Stock> findByProductCode(String productCode);

	@Modifying
	@Query("DELETE FROM Stock s WHERE s.product.productId = :productId")
	void deleteByProductId(@Param("productId") Long productId);
}