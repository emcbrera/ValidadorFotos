package com.ingsoftware.validador_fotos.dto.auth;

import lombok.Builder;

@Builder
public record ForgotPasswordResponse(
        String mensaje,
        String correo
) {
}
