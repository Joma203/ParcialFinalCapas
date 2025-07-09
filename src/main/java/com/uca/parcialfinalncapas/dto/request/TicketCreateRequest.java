
package com.uca.parcialfinalncapas.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TicketCreateRequest {
    @NotBlank
    private String titulo;

    @NotBlank
    private String descripcion;

    @NotBlank
    private String estado;


    private String correoUsuario;
}
