package org.zerock.b02.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Customer extends BaseEntity implements Serializable {

    @Id
    @Column(length = 50, nullable = false)
    private String customerId;
    @Column(length = 50, nullable = false)
    private String customerName;
    @Column(length = 100, nullable = false)
    private String customerInfo;

    private static final AtomicInteger customerIdCounter = new AtomicInteger(1);

    @PrePersist
    public synchronized void prePersist() {
        if (this.customerId == null) {
            this.customerId = generateCustomerId();
        }
    }

    private String generateCustomerId() {
        return "cust" + generateRandomAlphaNumeric(4);
    }

    private String generateRandomAlphaNumeric(int length) {
        String characters = "ABCDEFGH0123456789";
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    public void change(String customerId, String customerName, String customerInfo){
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerInfo = customerInfo;
    }
}