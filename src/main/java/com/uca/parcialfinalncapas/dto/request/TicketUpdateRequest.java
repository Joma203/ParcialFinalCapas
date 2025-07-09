
package com.uca.parcialfinalncapas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketUpdateRequest {
    @NotNull
    private Long id;

    @NotBlank
    private String estado;

}
