package com.ingsoftware.validador_fotos.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank(message = "El token es obligatorio")
        String token,
        @NotBlank(message = "La nueva contrasena es obligatoria")
        @Size(min = 8, message = "La nueva contrasena debe tener minimo 8 caracteres")
        String nuevaPassword
) {
}
