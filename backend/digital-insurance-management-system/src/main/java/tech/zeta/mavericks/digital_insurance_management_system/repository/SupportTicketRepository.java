package tech.zeta.mavericks.digital_insurance_management_system.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.zeta.mavericks.digital_insurance_management_system.model.SupportTicket;

@Repository public interface SupportTicketRepository  extends JpaRepository<SupportTicket, Long> { }
