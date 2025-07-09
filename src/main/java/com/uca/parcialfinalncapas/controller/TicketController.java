// src/main/java/com/uca/parcialfinalncapas/controller/TicketController.java
package com.uca.parcialfinalncapas.controller;

import com.uca.parcialfinalncapas.dto.request.TicketCreateRequest;
import com.uca.parcialfinalncapas.dto.request.TicketUpdateRequest;
import com.uca.parcialfinalncapas.dto.response.GeneralResponse;
import com.uca.parcialfinalncapas.dto.response.TicketResponseList;
import com.uca.parcialfinalncapas.exceptions.BadTicketRequestException;
import com.uca.parcialfinalncapas.service.TicketService;
import com.uca.parcialfinalncapas.utils.ResponseBuilderUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    @PreAuthorize("hasRole('TECH')")
    public ResponseEntity<GeneralResponse> getAllTickets() {
        List<TicketResponseList> tickets = ticketService.getAllTickets();
        return ResponseBuilderUtil.buildResponse(
                "Tickets obtenidos correctamente",
                tickets.isEmpty() ? HttpStatus.BAD_REQUEST : HttpStatus.OK,
                tickets
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('TECH')")
    public ResponseEntity<GeneralResponse> getTicketById(@PathVariable Long id) {
        var ticket = ticketService.getTicketById(id);
        if (ticket == null) {
            throw new BadTicketRequestException("Ticket no encontrado");
        }
        return ResponseBuilderUtil.buildResponse("Ticket encontrado", HttpStatus.OK, ticket);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GeneralResponse> getMyTickets(Authentication auth) {
        List<TicketResponseList> tickets = ticketService.getAllTickets().stream()
                .filter(t -> t.getCorreoUsuario().equals(auth.getName()))
                .collect(Collectors.toList());
        return ResponseBuilderUtil.buildResponse(
                "Mis tickets obtenidos correctamente",
                tickets.isEmpty() ? HttpStatus.BAD_REQUEST : HttpStatus.OK,
                tickets
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GeneralResponse> createTicket(
            @Valid @RequestBody TicketCreateRequest ticket,
            Authentication auth
    ) {
        ticket.setCorreoUsuario(auth.getName());
        var created = ticketService.createTicket(ticket);
        return ResponseBuilderUtil.buildResponse(
                "Ticket creado correctamente",
                HttpStatus.CREATED,
                created
        );
    }

    @PutMapping
    @PreAuthorize("hasRole('TECH')")
    public ResponseEntity<GeneralResponse> updateTicket(
            @Valid @RequestBody TicketUpdateRequest ticket) {
        var updated = ticketService.updateTicket(ticket);
        return ResponseBuilderUtil.buildResponse(
                "Ticket actualizado correctamente",
                HttpStatus.OK,
                updated
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TECH')")
    public ResponseEntity<GeneralResponse> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseBuilderUtil.buildResponse(
                "Ticket eliminado correctamente",
                HttpStatus.OK,
                null
        );
    }
}
