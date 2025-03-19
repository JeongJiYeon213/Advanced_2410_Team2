package org.zerock.b02.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.b02.domain.Inbound;

import java.time.LocalDateTime;

public interface InboundSearch {
    Page<Inbound> searchAll(String[] types, String keyword, Pageable pageable, LocalDateTime from, LocalDateTime to);
}
