package org.zerock.b02.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Admin extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 50, nullable = false)
    private String adminName;

    private Long adminId;

    private int adminPassword;

    private String position;

    private String phoneNum;

    public void change(String adminName, Long adminId, int adminPassword, String position, String phoneNum) {
        this.adminName = adminName;
        this.adminId = adminId;
        this.adminPassword = adminPassword;
        this.position = position;
        this.phoneNum = phoneNum;
    }

}
