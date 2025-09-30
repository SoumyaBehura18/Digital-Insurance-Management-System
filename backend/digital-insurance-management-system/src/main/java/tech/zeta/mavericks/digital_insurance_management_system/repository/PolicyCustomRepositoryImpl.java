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

/**
 * Implementation of custom repository methods for fetching insurance policies
 * based on user-specific criteria.
 *
 * This repository provides methods to filter and retrieve:
 * - Vehicle policies based on vehicle age
 * - Life policies based on user's smoking/drinking habits
 * - Health policies based on user's smoking/drinking habits and preexisting conditions
 *
 * Each method returns a list of {@link PolicyWithPremium} DTOs containing
 * policy details along with calculated premiums.
 */
@Repository
public class PolicyCustomRepositoryImpl implements PolicyRepository.PolicyCustomRepository {

    @PersistenceContext
    public EntityManager em;

    /**
     * Fetches all types of policies applicable for a given user request.
     *
     * Combines vehicle, life, and health policies into a single list.
     *
     * @param policyRequest DTO containing user-specific policy criteria
     * @return list of policies with calculated premiums
     */
    @Override
    public List<PolicyWithPremium> findPoliciesForUser(PolicyRequest policyRequest) {
        List<PolicyWithPremium> allPolicies = new ArrayList<>();
        allPolicies.addAll(findVehiclePoliciesForUser(policyRequest));
        allPolicies.addAll(findLifePoliciesForUser(policyRequest));
        allPolicies.addAll(findHealthPoliciesForUser(policyRequest));

        return allPolicies;
    }

    /**
     * Fetches vehicle policies applicable for a user based on vehicle age.
     *
     * @param policyRequest DTO containing user's vehicle age
     * @return list of vehicle policies with premiums
     */
    @Override
    public List<PolicyWithPremium> findVehiclePoliciesForUser(PolicyRequest policyRequest) {
        List<VehiclePolicyPremium> vehiclePolicies = em.createQuery(
                        "SELECT v FROM VehiclePolicyPremium v JOIN FETCH v.policy " +
                                "WHERE v.vehicleAge >= :vehicleAge and :vehicleAge!=0", VehiclePolicyPremium.class)
                .setParameter("vehicleAge", policyRequest.getVehicleAge())
                .getResultList();

        return vehiclePolicies.stream()
                .map(v -> new PolicyWithPremium(
                        v.getPolicy().getId(),
                        v.getPolicy().getName(),
                        v.getPolicy().getType(),
                        v.getPremiumRate(),
                        v.getRenewalRate(),
                        v.getPolicy().getDurationMonths(),
                        v.getPolicy().getCoverageAmt()))
                .collect(Collectors.toList());
    }

    /**
     * Fetches life insurance policies applicable for a user based on
     * their smoking/drinking habits.
     *
     * @param policyRequest DTO containing user's smoking/drinking status
     * @return list of life policies with premiums
     */
    @Override
    public List<PolicyWithPremium> findLifePoliciesForUser(PolicyRequest policyRequest) {
        List<LifePolicyPremium> lifePolicies = em.createQuery(
                        "SELECT l FROM LifePolicyPremium l JOIN FETCH l.policy " +
                                "WHERE l.smokingDrinking = :smokingDrinking",
                        LifePolicyPremium.class)
                .setParameter("smokingDrinking", policyRequest.getSmokingDrinking())
                .getResultList();

        return lifePolicies.stream()
                .map(l -> new PolicyWithPremium(
                        l.getPolicy().getId(),
                        l.getPolicy().getName(),
                        l.getPolicy().getType(),
                        l.getPremiumRate(),
                        l.getRenewalRate(),
                        l.getPolicy().getDurationMonths(),
                        l.getPolicy().getCoverageAmt()))
                .collect(Collectors.toList());
    }

    /**
     * Fetches health insurance policies applicable for a user based on
     * smoking/drinking habits and preexisting health conditions.
     *
     * Premiums are calculated as the base premium plus additional premiums
     * for each preexisting condition the user has.
     *
     * @param policyRequest DTO containing user's smoking/drinking status and preexisting conditions
     * @return list of health policies with calculated total premiums
     */
    @Override
    public List<PolicyWithPremium> findHealthPoliciesForUser(PolicyRequest policyRequest) {
        List<HealthPolicyPremium> healthPolicies = em.createQuery(
                "SELECT h FROM HealthPolicyPremium h JOIN FETCH h.policy",
                HealthPolicyPremium.class
        ).getResultList();

        return healthPolicies.stream()
                .filter(h -> Boolean.TRUE.equals(h.getSmokingDrinking()) == Boolean.TRUE.equals(policyRequest.getSmokingDrinking()))
                .map(h -> {
                    double basePremium = h.getPremiumRate() != null ? h.getPremiumRate() : 0.0;
                    double renewalRate = h.getRenewalRate() != null ? h.getRenewalRate() : 0.0;

                    List<HealthPreexistingCondition> policyConditions = h.getPolicy().getPreexistingConditions();

                    double additionalPremium = 0.0;
                    double additionalRenewal = 0.0;

                    if (policyConditions != null && policyRequest.getPreexistingConditions() != null) {
                        additionalPremium = policyConditions.stream()
                                .filter(cond -> policyRequest.getPreexistingConditions().contains(cond.getCondition()))
                                .mapToDouble(HealthPreexistingCondition::getAdditionalPremium)
                                .sum();
                        additionalRenewal = policyConditions.stream()
                                .filter(cond -> policyRequest.getPreexistingConditions().contains(cond.getCondition()))
                                .mapToDouble(HealthPreexistingCondition::getAdditionalPremium)
                                .sum();
                    }

                    double totalPremium = basePremium + additionalPremium;
                    double totalRenewalPremium = renewalRate + additionalRenewal;

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
    }
}
