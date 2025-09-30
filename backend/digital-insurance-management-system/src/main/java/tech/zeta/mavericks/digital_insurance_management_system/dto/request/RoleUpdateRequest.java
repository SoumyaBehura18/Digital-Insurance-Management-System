package tech.zeta.mavericks.digital_insurance_management_system.dto.request;

import lombok.Data;

/**
 * Data Transfer Object for updating a user's role.
 *
 * This DTO is used when modifying the role of a user in the system,
 * such as changing between ADMIN, USER, or other role types.
 */
@Data
public class RoleUpdateRequest {

    /** New role type to assign to the user */
    private String roleType;
}
