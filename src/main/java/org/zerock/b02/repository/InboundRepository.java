package org.zerock.b02.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.zerock.b02.domain.Inbound;
import org.zerock.b02.repository.search.InboundSearch;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface InboundRepository extends JpaRepository<Inbound, Long>, InboundSearch {

    Optional<Inbound> findTopByOrderByInboundCodeDesc();

    Page<Inbound> findByInboundDateBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);

    Page<Inbound> findByInboundDateAfter(LocalDateTime from, Pageable pageable);

    Page<Inbound> findByInboundDateBefore(LocalDateTime to, Pageable pageable);

    @Query("SELECT i FROM Inbound i JOIN FETCH i.product WHERE i.inboundId = :inboundId")
    Optional<Inbound> findByInboundIdWithProduct(Long inboundId);
}
