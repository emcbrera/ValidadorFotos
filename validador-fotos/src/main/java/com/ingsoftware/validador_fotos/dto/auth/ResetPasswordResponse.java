package com.ingsoftware.validador_fotos.dto.auth;

import lombok.Builder;

@Builder
public record ResetPasswordResponse(
        String mensaje,
        String correo
) {
}
