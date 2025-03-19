package org.zerock.b02.repository.search;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.zerock.b02.domain.Stock;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StockSearchImpl implements StockSearch {
	private final EntityManager entityManager;

	@Override
	public Page<Stock> searchAll(String[] types, String keyword, Pageable pageable) {
		StringBuilder jpql = new StringBuilder("SELECT s FROM Stock s WHERE s.stockId > 0");

		if ((types != null && types.length > 0) && keyword != null) {
			jpql.append(" AND (");
			for (int i = 0; i < types.length; i++) {
				String type = types[i];
				switch (type) {
					case "p":
						jpql.append("s.product.productName LIKE :keyword");
						break;
					case "c":
						jpql.append("s.product.productCode LIKE :keyword");
						break;
				}
				if (i < types.length - 1) {
					jpql.append(" OR ");
				}
			}
			jpql.append(")");
		}
		jpql.append(" ORDER BY s.stockId DESC");

		TypedQuery<Stock> query = entityManager.createQuery(jpql.toString(), Stock.class);
		TypedQuery<Long> countQuery = entityManager.createQuery(
				jpql.toString().replace("SELECT s", "SELECT COUNT(s)"), Long.class);

		if ((types != null && types.length > 0) && keyword != null) {
			query.setParameter("keyword", "%" + keyword + "%");
			countQuery.setParameter("keyword", "%" + keyword + "%");
		}
		query.setFirstResult((int) pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());

		List<Stock> list = query.getResultList();
		long count = countQuery.getSingleResult();

		return new PageImpl<>(list, pageable, count);
	}
}