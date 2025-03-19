package org.zerock.b02.repository.search;

import org.zerock.b02.domain.Customer;
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
public class CustomerSearchImpl implements CustomerSearch {
    private final EntityManager entityManager;

    @Override
    public Page<Customer> searchAll(String[] types, String keyword, Pageable pageable) {
        StringBuilder jpql = new StringBuilder("SELECT c FROM Customer c WHERE c.customerId IS NOT NULL");

        if ((types != null && types.length > 0) && keyword != null) {
            jpql.append(" AND (");

            for (int i = 0; i < types.length; i++) {
                String type = types[i];
                switch (type) {
                    case "i":
                        jpql.append("c.customerId LIKE :keyword");
                        break;
                    case "n":
                        jpql.append("c.customerName LIKE :keyword");
                        break;
                    case "f":
                        jpql.append("c.customerInfo LIKE :keyword");
                        break;
                }

                if (i < types.length - 1) {
                    jpql.append(" OR ");
                }
            }
            jpql.append(")");
        }
        jpql.append(" ORDER BY c.customerName ASC");

        TypedQuery<Customer> query = entityManager.createQuery(jpql.toString(), Customer.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(
                jpql.toString().replace("SELECT c", "SELECT COUNT(c)"), Long.class
        );

        if ((types != null && types.length > 0) && keyword != null) {
            query.setParameter("keyword", "%" + keyword + "%");
            countQuery.setParameter("keyword", "%" + keyword + "%");
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Customer> list = query.getResultList();
        long count = countQuery.getSingleResult();

        return new PageImpl<>(list, pageable, count);
    }
}