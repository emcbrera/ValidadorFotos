
package com.ingsoftware.validador_fotos.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El usuario o correo es obligatorio")
        String identificador,
        @NotBlank(message = "La contrasena es obligatoria")
        String password
) {
}
