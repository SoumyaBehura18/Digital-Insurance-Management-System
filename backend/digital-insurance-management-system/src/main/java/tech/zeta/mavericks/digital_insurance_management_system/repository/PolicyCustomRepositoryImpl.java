package tech.zeta.mavericks.digital_insurance_management_system.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.PolicyRequest;
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
    public EntityManager em;

    @Override
    public List<PolicyWithPremiumDTO> findPoliciesForUser(PolicyRequest policyRequest) {

        List<PolicyWithPremiumDTO> allPolicies = new ArrayList<>();
        allPolicies.addAll(findVehiclePoliciesForUser(policyRequest));
        allPolicies.addAll(findLifePoliciesForUser(policyRequest));
        allPolicies.addAll(findHealthPoliciesForUser(policyRequest));

        return allPolicies;
    }

    @Override
    public List<PolicyWithPremiumDTO> findVehiclePoliciesForUser(PolicyRequest policyRequest) {
        List<VehiclePolicyPremium> vehiclePolicies = em.createQuery(
                        "SELECT v FROM VehiclePolicyPremium v JOIN FETCH v.policy WHERE v.vehicleAge >= :vehicleAge", VehiclePolicyPremium.class)
                .setParameter("vehicleAge", policyRequest.getVehicleAge())
                .getResultList();

        List<PolicyWithPremiumDTO> dtos = vehiclePolicies.stream()
                .map(v -> new PolicyWithPremiumDTO(
                        v.getPolicy().getId(),
                        v.getPolicy().getName(),
                        v.getPolicy().getType(),
                        v.getPremiumRate(),
                        v.getRenewalRate(),
                        v.getPolicy().getDurationMonths(),
                        v.getPolicy().getCoverageAmt()))
                .collect(Collectors.toList());
        return dtos;
    }

    @Override
    public List<PolicyWithPremiumDTO> findLifePoliciesForUser(PolicyRequest policyRequest) {
        System.out.println("Smoking drinking: "+policyRequest);
        List<LifePolicyPremium> lifePolicies = em.createQuery(
                        "SELECT l FROM LifePolicyPremium l JOIN FETCH l.policy " +
                                "WHERE l.smokingDrinking = :smokingDrinking",
                        LifePolicyPremium.class)
                .setParameter("smokingDrinking", policyRequest.getSmokingDrinking())
                .getResultList();

        List<PolicyWithPremiumDTO> dtos = lifePolicies.stream()
                .map(l -> new PolicyWithPremiumDTO(
                        l.getPolicy().getId(),
                        l.getPolicy().getName(),
                        l.getPolicy().getType(),
                        l.getPremiumRate(),
                        l.getRenewalRate(),
                        l.getPolicy().getDurationMonths(),
                        l.getPolicy().getCoverageAmt()))
                .collect(Collectors.toList());

        return  dtos;

    }

    @Override
    public List<PolicyWithPremiumDTO> findHealthPoliciesForUser(PolicyRequest policyRequest) {
        List<HealthPolicyPremium> healthPolicies = em.createQuery(
                "SELECT h FROM HealthPolicyPremium h JOIN FETCH h.policy",
                HealthPolicyPremium.class
        ).getResultList();

        List<PolicyWithPremiumDTO> dtos = healthPolicies.stream()
                // Filter for smoker/non-smoker
                .filter(h -> Boolean.TRUE.equals(h.getSmokingDrinking()) == Boolean.TRUE.equals(policyRequest.getSmokingDrinking()))
                .map(h -> {
                    // Base premium for this policy
                    double basePremium = h.getPremiumRate() != null ? h.getPremiumRate() : 0.0;
                    double renewalRate = h.getRenewalRate() != null ? h.getRenewalRate() : 0.0;

                    // All preexisting conditions for this policy
                    List<HealthPreexistingCondition> policyConditions = h.getPolicy().getPreexistingConditions();

                    // Sum only premiums for the conditions the user has
                    double additionalPremium = 0.0;
                    double additionalRenewal=0.0;
                    if (policyConditions != null && policyRequest.getPreexistingConditions() != null) {
                        additionalPremium = policyConditions.stream()
                                .filter(cond -> policyRequest.getPreexistingConditions().contains(cond.getCondition()))
                                .mapToDouble(HealthPreexistingCondition::getAdditionalPremium)
                                .sum();
                        additionalRenewal=policyConditions.stream()
                                .filter(cond -> policyRequest.getPreexistingConditions().contains(cond.getCondition()))
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
                            h.getPolicy().getDurationMonths(),
                            h.getPolicy().getCoverageAmt()
                    );
                })
                .collect(Collectors.toList());
        return dtos;
    }
}
