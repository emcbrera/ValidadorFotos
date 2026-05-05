package com.ingsoftware.validador_fotos.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RechazarFotoRequest(
        @NotBlank(message = "La observacion es obligatoria")
        @Size(max = 500, message = "La observacion no puede superar los 500 caracteres")
        String observacion
) {
}
