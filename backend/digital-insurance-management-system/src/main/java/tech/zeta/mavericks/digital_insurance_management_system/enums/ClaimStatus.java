package tech.zeta.mavericks.digital_insurance_management_system.enums;

/**
 * Enum representing the status of an insurance claim.
 * This is used to track the lifecycle of a claim from submission to resolution.
 *
 * PENDING: Claim is submitted and awaiting review.
 * APPROVED: Claim has been reviewed and approved for settlement.
 * REJECTED: Claim has been reviewed and denied.
 *
 * Usage: Used in Claim entity and related DTOs.
 */
public enum ClaimStatus {
    PENDING,
    APPROVED,
    REJECTED
}
