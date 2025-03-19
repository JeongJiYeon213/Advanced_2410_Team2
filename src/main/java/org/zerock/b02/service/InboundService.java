package org.zerock.b02.service;

import org.zerock.b02.dto.InboundDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;

import java.util.List;

public interface InboundService {
    // 등록
    Long register(InboundDTO inboundDTO);
    // 1행 조회
    InboundDTO readOne(Long inboundId);
    // 1행 수정
    void modify(InboundDTO inboundDTO);
    // 1행 삭제
    void remove(Long inboundId);

    PageResponseDTO<InboundDTO> list(PageRequestDTO pageRequestDTO);

    List<InboundDTO> getAllInbound();
}
