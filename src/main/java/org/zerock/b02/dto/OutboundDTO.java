package org.zerock.b02.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutboundDTO {
    private Long outboundId;
    @Setter
    private String outboundCode;
    private String productCode;
    private String customerId;
    private Long quantity;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime outboundDate;

    private String description;
    private String outboundStatus;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}