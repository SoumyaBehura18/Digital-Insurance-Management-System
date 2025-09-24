package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vehicle_policy_premiums")
public class VehiclePolicyPremium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    private Double premiumRate;

    private Double renewalRate;

    private Integer vehicleAge;

    private Boolean noClaimBonus;

    private Double idv; // Insured Declared Value

}

