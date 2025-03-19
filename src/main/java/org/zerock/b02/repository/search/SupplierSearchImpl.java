package org.zerock.b02.repository.search;

import org.zerock.b02.domain.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierSearchImpl implements SupplierSearch {
    private final EntityManager entityManager;

    @Override
    public Page<Supplier> searchAll(String[] types, String keyword, Pageable pageable) {
        StringBuilder jpql = new StringBuilder("SELECT b FROM Supplier b WHERE b.supplierId IS NOT NULL");

        if ((types != null && types.length > 0) && keyword != null) {
            jpql.append(" AND (");

            for (int i = 0; i < types.length; i++) {
                String type = types[i];
                switch (type) {
                    case "i":
                        jpql.append("b.supplierId LIKE :keyword");
                        break;
                    case "n":
                        jpql.append("b.supplierName LIKE :keyword");
                        break;
                    case "f":
                        jpql.append("b.supplierInfo LIKE :keyword");
                        break;
                }

                if (i < types.length - 1) {
                    jpql.append(" OR ");
                }
            }
            jpql.append(")");
        }

        jpql.append(" ORDER BY b.supplierName ASC");

        TypedQuery<Supplier> query = entityManager.createQuery(jpql.toString(), Supplier.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(
                jpql.toString().replace("SELECT b", "SELECT COUNT(b)"), Long.class
        );

        if ((types != null && types.length > 0) && keyword != null) {
            query.setParameter("keyword", "%" + keyword + "%");
            countQuery.setParameter("keyword", "%" + keyword + "%");
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Supplier> list = query.getResultList();
        long count = countQuery.getSingleResult();

        return new PageImpl<>(list, pageable, count);
    }
}