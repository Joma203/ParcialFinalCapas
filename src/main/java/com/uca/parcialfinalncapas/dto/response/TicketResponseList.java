
package com.uca.parcialfinalncapas.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketResponseList {
    private Long id;
    private String titulo;
    private String descripcion;
    private String estado;
    private String correoUsuario;   // para filtrar en /my
    private String correoSoporte;   // si lo incluyes
    private LocalDateTime fecha;
}
