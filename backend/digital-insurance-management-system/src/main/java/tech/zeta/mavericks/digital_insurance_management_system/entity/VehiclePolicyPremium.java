package tech.zeta.mavericks.digital_insurance_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entity representing vehicle-specific premium details for a policy.
 *
 * Stores the associated policy, premium rate, renewal rate, and vehicle age
 * to calculate insurance costs for vehicle policies.
 */
@Data
@Entity
@Table(name = "vehicle_policy_premium")
public class VehiclePolicyPremium {

    /** Unique ID of the vehicle policy premium record */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Policy associated with this vehicle premium */
    @ManyToOne(optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    /** Premium rate for the vehicle policy */
    private Double premiumRate;

    /** Renewal rate for the vehicle policy */
    private Double renewalRate;

    /** Age of the vehicle in years */
    private Integer vehicleAge;
}
