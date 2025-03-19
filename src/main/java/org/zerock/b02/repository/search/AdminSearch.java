package org.zerock.b02.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.b02.domain.Admin;

public interface AdminSearch {
    Page<Admin> searchAll(String[] types, String keyword, Pageable pageable);
}
