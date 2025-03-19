package org.zerock.b02.repository.search;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.b02.domain.Outbound;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboundSearchImpl implements OutboundSearch {
    private final EntityManager entityManager;

    @Override
    public Page<Outbound> searchAll(String[] types, String keyword, Pageable pageable, LocalDateTime from, LocalDateTime to) {
        StringBuilder jpql = new StringBuilder("SELECT o FROM Outbound o WHERE o.outboundId > 0");

        if (from != null && to != null) {
            jpql.append(" AND o.outboundDate BETWEEN :from AND :to");
        } else if (from != null) {
            jpql.append(" AND o.outboundDate >= :from");
        } else if (to != null) {
            jpql.append(" AND o.outboundDate <= :to");
        }

        if ((types != null && types.length > 0) && keyword != null) {
            jpql.append(" AND (");

            for (int o = 0; o < types.length; o++) {
                String type = types[o];
                switch (type) {
                    case "a":
                        jpql.append("o.outboundCode LIKE :keyword");
                        break;
                    case "b":
                        jpql.append("o.product.productCode LIKE :keyword");
                        break;
                    case "c":
                        jpql.append("o.customer.customerId LIKE :keyword");
                        break;
                    case "d":
                        jpql.append("o.inboundStatus LIKE :keyword");
                        break;
                }

                if (o < types.length - 1) {
                    jpql.append(" OR ");
                }
            }
            jpql.append(")");
        }
        jpql.append(" ORDER BY outboundId DESC");

        TypedQuery<Outbound> query = entityManager.createQuery(jpql.toString(), Outbound.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(
                jpql.toString().replace("SELECT o", "SELECT COUNT(o)"), Long.class
        );

        if ((types != null && types.length > 0) && keyword != null) {
            query.setParameter("keyword", "%" + keyword + "%");
            countQuery.setParameter("keyword", "%" + keyword + "%");
        }
        if (from != null) {
            query.setParameter("from", from);
            countQuery.setParameter("from", from);
        }
        if (to != null) {
            query.setParameter("to", to);
            countQuery.setParameter("to", to);
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Outbound> list = query.getResultList();
        long count = countQuery.getSingleResult();

        return new PageImpl<>(list, pageable, count);
    }
}