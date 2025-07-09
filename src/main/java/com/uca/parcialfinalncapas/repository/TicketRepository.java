// src/main/java/com/uca/parcialfinalncapas/repository/TicketRepository.java
package com.uca.parcialfinalncapas.repository;

import com.uca.parcialfinalncapas.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUsuarioId(Long usuarioId);
}
