package tech.zeta.mavericks.digital_insurance_management_system.repository.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import tech.zeta.mavericks.digital_insurance_management_system.dto.PolicyWithPremiumDTO;
import tech.zeta.mavericks.digital_insurance_management_system.entity.HealthPolicyPremium;
import tech.zeta.mavericks.digital_insurance_management_system.entity.LifePolicyPremium;
import tech.zeta.mavericks.digital_insurance_management_system.entity.VehiclePolicyPremium;
import tech.zeta.mavericks.digital_insurance_management_system.enums.HealthCondition;
import tech.zeta.mavericks.digital_insurance_management_system.enums.VehicleType;
import tech.zeta.mavericks.digital_insurance_management_system.repository.interfaces.PolicyCustomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class PolicyCustomRepositoryImpl implements PolicyCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<PolicyWithPremiumDTO> findPoliciesForUser(Boolean smokingDrinking, VehicleType vehicleType,
                                                          Integer vehicleAge, Set<HealthCondition> preexistingConditions) {

        List<PolicyWithPremiumDTO> allPolicies = new ArrayList<>();

        // --- VEHICLE POLICIES ---
//        List<VehiclePolicyPremium> vehiclePolicies = em.createQuery(
//                        "SELECT v FROM VehiclePolicyPremium v JOIN FETCH v.policy WHERE v.vehicleAge >= :vehicleAge", VehiclePolicyPremium.class)
//                .setParameter("vehicleAge", vehicleAge)
//                .getResultList();
//
//        List<PolicyWithPremiumDTO> dtos = vehiclePolicies.stream()
//                .map(v -> new PolicyWithPremiumDTO(
//                        v.getPolicy().getId(),
//                        v.getPolicy().getName(),
//                        v.getPolicy().getType(),
//                        v.getPremiumRate(),
//                        v.getRenewalRate()))
//                .collect(Collectors.toList());
//        allPolicies.addAll(dtos);

//        System.out.println("Smoking drinking: "+smokingDrinking);
//        // Fetch LifePolicyPremium entities based on smokingDrinking filter
//        List<LifePolicyPremium> lifePolicies = em.createQuery(
//                        "SELECT l FROM LifePolicyPremium l JOIN FETCH l.policy " +
//                                "WHERE l.smokingDrinking = :smokingDrinking",
//                        LifePolicyPremium.class)
//                .setParameter("smokingDrinking", smokingDrinking)
//                .getResultList();
//
//// Map the entities to DTOs
//        List<PolicyWithPremiumDTO> dtos = lifePolicies.stream()
//                .map(l -> new PolicyWithPremiumDTO(
//                        l.getPolicy().getId(),
//                        l.getPolicy().getName(),
//                        l.getPolicy().getType(),
//                        l.getPremiumRate(),
//                        l.getRenewalRate()))
//                .collect(Collectors.toList());
//
//// Add to your combined list if needed
//        allPolicies.addAll(dtos);


//// Health
//
// Filter by smokingDrinking and preexistingConditions if provided
        // Fetch HealthPolicyPremium entities
        List<HealthPolicyPremium> healthPolicies = em.createQuery(
                        "SELECT h FROM HealthPolicyPremium h JOIN FETCH h.policy",
                        HealthPolicyPremium.class)
                .getResultList();

//// Filter and map to DTO
//        List<PolicyWithPremiumDTO> dtos = healthPolicies.stream()
//                .filter(h -> h.getSmokingDrinking().equals(smokingDrinking)) // always true/false
//                .filter(h -> {
//                    if (preexistingConditions == null || preexistingConditions.isEmpty()) {
//                        return true;
//                    }
//                    // Check if the premium's preexisting diseases contain all requested conditions
//                    return h.getPreexistingDiseases().containsAll(preexistingConditions);
//                })
//                .map(h -> new PolicyWithPremiumDTO(
//                        h.getPolicy().getId(),
//                        h.getPolicy().getName(),
//                        h.getPolicy().getType(),
//                        h.getPremiumRate(),
//                        h.getRenewalRate()))
//                .collect(Collectors.toList());
//
//
//// Add to combined list
//        allPolicies.addAll(dtos);


        return allPolicies;
    }
}
