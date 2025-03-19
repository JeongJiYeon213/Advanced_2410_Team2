package org.zerock.b02.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
    private Long bno;

    @NotEmpty
    private String adminName;

    @NotNull
    private Long adminId;

    @NotNull
    private int adminPassword;

    @NotEmpty
    private String position;

    @NotEmpty
    private String phoneNum;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

}
