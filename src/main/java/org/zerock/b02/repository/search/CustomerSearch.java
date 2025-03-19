package org.zerock.b02.repository.search;

import org.zerock.b02.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerSearch {

    Page<Customer> searchAll(String[] types, String keyword, Pageable pageable);
}
