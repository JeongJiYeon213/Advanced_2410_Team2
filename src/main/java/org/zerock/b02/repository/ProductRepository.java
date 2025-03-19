package org.zerock.b02.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.b02.domain.Product;
import org.zerock.b02.repository.search.ProductSearch;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearch {
	@Query("SELECT p FROM Product p WHERE p.productName = :productName")
	List<Product> findByProductName(@Param("productName") String productName);

	@Query("SELECT p FROM Product p WHERE p.productType = :productType")
	List<Product> findByProductType(@Param("productType") String productType);

	@Query("select p from Product p where p.productName like concat('%', :keyword, '%')")
	Page<Product> findKeyword(@Param("keyword") String keyword, Pageable pageable);

	Optional<Product> findByProductCode(String productCode);
}
