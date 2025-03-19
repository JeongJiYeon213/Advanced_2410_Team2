package org.zerock.b02.service;


import org.zerock.b02.dto.OutboundDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;

import java.util.List;

public interface OutboundService {
    Long register(OutboundDTO outboundDTO);
    // 1행 조회
    OutboundDTO readOne(Long outboundId);
    // 1행 수정
    void modify(OutboundDTO outboundDTO);
    // 1행 삭제
    void remove(Long outboundId);

    PageResponseDTO<OutboundDTO> list(PageRequestDTO pageRequestDTO);

    List<OutboundDTO> getAllOutbound();
}
