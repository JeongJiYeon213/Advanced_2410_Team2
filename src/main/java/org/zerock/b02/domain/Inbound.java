package org.zerock.b02.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Getter
@Builder
@Table(name = "inbound")
public class Inbound extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inboundId;

    @Column(nullable = false, unique = true)
    private String inboundCode;

    @Setter
    @ManyToOne
    @JoinColumn(name = "productCode", referencedColumnName = "productCode", nullable = false)
    private Product product;

    @Setter
    @ManyToOne
    @JoinColumn(name = "supplierId", referencedColumnName = "supplierId", nullable = false)
    private Supplier supplier;

    @Setter
    @Column(nullable = false)
    private Long quantity;

    @Setter
    @Column(name = "inboundDate", nullable = false)
    private LocalDateTime inboundDate;

    @Setter
    @Column(length = 500)
    private String description;

    @Setter
    @Column(name = "status",nullable = false)
    private String inboundStatus;

    public void change(Product product,
                       String inboundCode,
                       Supplier supplier,
                       Long quantity,
                       LocalDateTime inboundDate,
                       String description,
                       String inboundStatus){
        this.inboundCode = inboundCode;
        this.product = product;
        this.supplier = supplier;
        this.quantity = quantity;
        this.inboundDate = inboundDate;
        this.description = description;
        this.inboundStatus = inboundStatus;
    }
}