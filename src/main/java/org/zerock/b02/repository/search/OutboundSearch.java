package org.zerock.b02.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.b02.domain.Outbound;

import java.time.LocalDateTime;

public interface OutboundSearch {
    Page<Outbound> searchAll(String[] types, String keyword, Pageable pageable, LocalDateTime from, LocalDateTime to);
}
