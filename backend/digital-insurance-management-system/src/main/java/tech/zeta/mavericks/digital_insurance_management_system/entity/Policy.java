package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private Double premiumRate;
    private Double renewalRate;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private PolicyType type;

    // Correct mapping to HealthPreexistingCondition
    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude  // Prevent recursion
    private List<HealthPreexistingCondition> preexistingConditions = new ArrayList<>();
}
