package tech.zeta.mavericks.digital_insurance_management_system.dto.request;

import lombok.Data;

/**
 * Data Transfer Object for submitting a message in a support ticket conversation.
 *
 * This DTO is used to capture message content and the author information
 * before processing in the service layer.
 */
@Data
public class MessageRequest {

    /** ID of the user or admin sending the message */
    private Long authorId;

    /** Text content of the message */
    private String content;
}
