package org.zerock.b02.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.b02.domain.Outbound;
import org.zerock.b02.repository.search.OutboundSearch;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OutboundRepository extends JpaRepository<Outbound, Long>, OutboundSearch {

    Optional<Outbound> findTopByOrderByOutboundCodeDesc();

    Page<Outbound> findByOutboundDateBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);

    Page<Outbound> findByOutboundDateAfter(LocalDateTime from, Pageable pageable);

    Page<Outbound> findByOutboundDateBefore(LocalDateTime to, Pageable pageable);
}
