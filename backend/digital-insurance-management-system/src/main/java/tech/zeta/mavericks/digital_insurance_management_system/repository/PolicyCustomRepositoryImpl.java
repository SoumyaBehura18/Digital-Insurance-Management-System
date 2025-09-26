package tech.zeta.mavericks.digital_insurance_management_system.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.PolicyWithPremiumDTO;
import tech.zeta.mavericks.digital_insurance_management_system.entity.HealthPolicyPremium;
import tech.zeta.mavericks.digital_insurance_management_system.entity.HealthPreexistingCondition;
import tech.zeta.mavericks.digital_insurance_management_system.entity.LifePolicyPremium;
import tech.zeta.mavericks.digital_insurance_management_system.entity.VehiclePolicyPremium;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
import tech.zeta.mavericks.digital_insurance_management_system.enums.VehicleType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class PolicyCustomRepositoryImpl implements PolicyRepository.PolicyCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<PolicyWithPremiumDTO> findPoliciesForUser(Boolean smokingDrinking, VehicleType vehicleType,
                                                          Integer vehicleAge, Set<HealthCondition> preexistingConditions) {

        List<PolicyWithPremiumDTO> allPolicies = new ArrayList<>();
        allPolicies.addAll(findVehiclePoliciesForUser(smokingDrinking, vehicleType, vehicleAge, preexistingConditions));
        allPolicies.addAll(findLifePoliciesForUser(smokingDrinking, vehicleType, vehicleAge, preexistingConditions));
        allPolicies.addAll(findHealthPoliciesForUser(smokingDrinking, vehicleType, vehicleAge, preexistingConditions));

        return allPolicies;
    }

    @Override
    public List<PolicyWithPremiumDTO> findVehiclePoliciesForUser(Boolean smokingDrinking, VehicleType vehicleType, Integer vehicleAge, Set<HealthCondition> preexistingConditions) {
        List<VehiclePolicyPremium> vehiclePolicies = em.createQuery(
                        "SELECT v FROM VehiclePolicyPremium v JOIN FETCH v.policy WHERE v.vehicleAge >= :vehicleAge", VehiclePolicyPremium.class)
                .setParameter("vehicleAge", vehicleAge)
                .getResultList();

        List<PolicyWithPremiumDTO> dtos = vehiclePolicies.stream()
                .map(v -> new PolicyWithPremiumDTO(
                        v.getPolicy().getId(),
                        v.getPolicy().getName(),
                        v.getPolicy().getType(),
                        v.getPremiumRate(),
                        v.getRenewalRate(),
                        v.getPolicy().getDurationMonths()))
                .collect(Collectors.toList());
        return dtos;
    }

    @Override
    public List<PolicyWithPremiumDTO> findLifePoliciesForUser(Boolean smokingDrinking, VehicleType vehicleType, Integer vehicleAge, Set<HealthCondition> preexistingConditions) {
        System.out.println("Smoking drinking: "+smokingDrinking);
        List<LifePolicyPremium> lifePolicies = em.createQuery(
                        "SELECT l FROM LifePolicyPremium l JOIN FETCH l.policy " +
                                "WHERE l.smokingDrinking = :smokingDrinking",
                        LifePolicyPremium.class)
                .setParameter("smokingDrinking", smokingDrinking)
                .getResultList();

        List<PolicyWithPremiumDTO> dtos = lifePolicies.stream()
                .map(l -> new PolicyWithPremiumDTO(
                        l.getPolicy().getId(),
                        l.getPolicy().getName(),
                        l.getPolicy().getType(),
                        l.getPremiumRate(),
                        l.getRenewalRate(),
                        l.getPolicy().getDurationMonths()))
                .collect(Collectors.toList());

        return  dtos;

    }

    @Override
    public List<PolicyWithPremiumDTO> findHealthPoliciesForUser(Boolean smokingDrinking, VehicleType vehicleType, Integer vehicleAge, Set<HealthCondition> preexistingConditions) {
        List<HealthPolicyPremium> healthPolicies = em.createQuery(
                "SELECT h FROM HealthPolicyPremium h JOIN FETCH h.policy",
                HealthPolicyPremium.class
        ).getResultList();

        List<PolicyWithPremiumDTO> dtos = healthPolicies.stream()
                // Filter for smoker/non-smoker
                .filter(h -> Boolean.TRUE.equals(h.getSmokingDrinking()) == Boolean.TRUE.equals(smokingDrinking))
                .map(h -> {
                    // Base premium for this policy
                    double basePremium = h.getPremiumRate() != null ? h.getPremiumRate() : 0.0;
                    double renewalRate = h.getRenewalRate() != null ? h.getRenewalRate() : 0.0;

                    // All preexisting conditions for this policy
                    List<HealthPreexistingCondition> policyConditions = h.getPolicy().getPreexistingConditions();

                    // Sum only premiums for the conditions the user has
                    double additionalPremium = 0.0;
                    double additionalRenewal=0.0;
                    if (policyConditions != null && preexistingConditions != null) {
                        additionalPremium = policyConditions.stream()
                                .filter(cond -> preexistingConditions.contains(cond.getCondition()))
                                .mapToDouble(HealthPreexistingCondition::getAdditionalPremium)
                                .sum();
                        additionalRenewal=policyConditions.stream()
                                .filter(cond -> preexistingConditions.contains(cond.getCondition()))
                                .mapToDouble(HealthPreexistingCondition::getAdditionalPremium)
                                .sum();
                    }

                    // Total premium = base + additional
                    double totalPremium = basePremium + additionalPremium;
                    double totalRenewalPremium=renewalRate+additionalRenewal;

                    // Create DTO
                    return new PolicyWithPremiumDTO(
                            h.getPolicy().getId(),
                            h.getPolicy().getName(),
                            h.getPolicy().getType(),
                            totalPremium,
                            totalRenewalPremium,
                            h.getPolicy().getDurationMonths()
                    );
                })
                .collect(Collectors.toList());
        return dtos;
    }
}
