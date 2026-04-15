package com.ingsoftware.validador_fotos.dto.auth;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ForgotPasswordResponse(
        String mensaje,
        String correo,
        String tokenRecuperacion,
        LocalDateTime expiraEn,
        String enlaceRecuperacion
) {
}
