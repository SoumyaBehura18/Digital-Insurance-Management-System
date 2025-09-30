package tech.zeta.mavericks.digital_insurance_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Data Transfer Object for returning messages in a support ticket conversation.
 *
 * This DTO is used to provide message details, including the author, content, and timestamp,
 * when retrieving messages from the service layer.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {

    /** Unique ID of the message */
    private Long id;

    /** ID of the user or admin who authored the message */
    private Long authorId;

    /** Text content of the message */
    private String content;

    /** Timestamp when the message was created */
    private Timestamp timestamp;
}
