package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyType;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "policies")
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double coverageAmt;

    private Integer durationMonths;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private PolicyType type;

}
