package org.zerock.b02.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InboundDTO {
    private Long inboundId;
    @Setter
    private String inboundCode;
    private String productCode;
    private String supplierId;
    private Long quantity;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime inboundDate;

    private String description;
    private String inboundStatus;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
