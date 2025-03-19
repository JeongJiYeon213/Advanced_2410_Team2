package org.zerock.b02.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Supplier extends BaseEntity implements Serializable {

    @Id
    @Column(length = 50, nullable = false)
    private String supplierId;
    @Column(length = 50, nullable = false)
    private String supplierName;
    @Column(length = 50, nullable = false)
    private String supplierInfo;

    public void change(String supplierId, String supplierName, String supplierInfo) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.supplierInfo = supplierInfo;
    }

}