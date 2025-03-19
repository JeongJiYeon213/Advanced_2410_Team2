package org.zerock.b02.repository.search;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.b02.domain.Product;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductSearchImpl implements ProductSearch {
	private final EntityManager entityManager;

	@Override
	public Page<Product> searchAll(String[] types, String keyword, Pageable pageable) {
		StringBuilder jpql = new StringBuilder("SELECT p FROM Product p WHERE p.productId > 0");

		if ((types != null && types.length > 0) && keyword != null) {
			jpql.append(" AND (");

			for (int i = 0; i < types.length; i++) {
				String type = types[i];
				switch (type) {
					case "n":
						jpql.append("p.productName LIKE :keyword");
						break;
					case "c":
						jpql.append("p.productCode LIKE :keyword");
						break;
				}
				if (i < types.length - 1) {
					jpql.append(" OR ");
				}
			}
			jpql.append(")");
		}
		jpql.append("ORDER BY productId DESC");

		TypedQuery<Product> query = entityManager.createQuery(jpql.toString(), Product.class);
		TypedQuery<Long> countQuery = entityManager.createQuery(
				jpql.toString().replace("SELECT p", "SELECT COUNT(p)"), Long.class);


		if ((types != null && types.length > 0) && keyword != null) {
			query.setParameter("keyword", "%" + keyword + "%");
			countQuery.setParameter("keyword", "%" + keyword + "%");
		}
		query.setFirstResult((int) pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());

		List<Product> list = query.getResultList();
		long count = countQuery.getSingleResult();

		return new PageImpl<>(list, pageable, count);
	}
}
