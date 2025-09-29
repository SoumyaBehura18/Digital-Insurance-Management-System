package tech.zeta.mavericks.digital_insurance_management_system.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.PolicyRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.PolicyWithPremium;
import tech.zeta.mavericks.digital_insurance_management_system.entity.HealthPolicyPremium;
import tech.zeta.mavericks.digital_insurance_management_system.entity.HealthPreexistingCondition;
import tech.zeta.mavericks.digital_insurance_management_system.entity.LifePolicyPremium;
import tech.zeta.mavericks.digital_insurance_management_system.entity.VehiclePolicyPremium;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PolicyCustomRepositoryImpl implements PolicyRepository.PolicyCustomRepository {

    @PersistenceContext
    public EntityManager em;

    @Override
    public List<PolicyWithPremium> findPoliciesForUser(PolicyRequest policyRequest) {

        List<PolicyWithPremium> allPolicies = new ArrayList<>();
        allPolicies.addAll(findVehiclePoliciesForUser(policyRequest));
        allPolicies.addAll(findLifePoliciesForUser(policyRequest));
        allPolicies.addAll(findHealthPoliciesForUser(policyRequest));

        return allPolicies;
    }

    @Override
    public List<PolicyWithPremium> findVehiclePoliciesForUser(PolicyRequest policyRequest) {
        List<VehiclePolicyPremium> vehiclePolicies = em.createQuery(
                        "SELECT v FROM VehiclePolicyPremium v JOIN FETCH v.policy WHERE v.vehicleAge >= :vehicleAge and :vehicleAge!=0", VehiclePolicyPremium.class)
                .setParameter("vehicleAge", policyRequest.getVehicleAge())
                .getResultList();

        List<PolicyWithPremium> dtos = vehiclePolicies.stream()
                .map(v -> new PolicyWithPremium(
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
    public List<PolicyWithPremium> findLifePoliciesForUser(PolicyRequest policyRequest) {
        System.out.println("Smoking drinking: "+policyRequest);
        List<LifePolicyPremium> lifePolicies = em.createQuery(
                        "SELECT l FROM LifePolicyPremium l JOIN FETCH l.policy " +
                                "WHERE l.smokingDrinking = :smokingDrinking",
                        LifePolicyPremium.class)
                .setParameter("smokingDrinking", policyRequest.getSmokingDrinking())
                .getResultList();

        List<PolicyWithPremium> dtos = lifePolicies.stream()
                .map(l -> new PolicyWithPremium(
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
    public List<PolicyWithPremium> findHealthPoliciesForUser(PolicyRequest policyRequest) {
        List<HealthPolicyPremium> healthPolicies = em.createQuery(
                "SELECT h FROM HealthPolicyPremium h JOIN FETCH h.policy",
                HealthPolicyPremium.class
        ).getResultList();

        List<PolicyWithPremium> dtos = healthPolicies.stream()
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
                    return new PolicyWithPremium(
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
