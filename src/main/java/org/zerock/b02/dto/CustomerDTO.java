package org.zerock.b02.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.b02.domain.Customer;

import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    @Id
    @Size(min = 1, max = 10)
    private String customerId;

    @NotEmpty(message = "고객명을 입력하세요.")
    private String customerName;

    @NotEmpty(message = "전화번호를 입력하세요.")
    private String customerInfo;

    private String customerSize;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    public Customer toEntity() {
        return Customer.builder()
                .customerName(this.customerName)
                .customerInfo(this.customerInfo)
                .build();
    }

}
