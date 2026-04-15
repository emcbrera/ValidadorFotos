
package com.ingsoftware.validador_fotos.dto.auth;

import lombok.Builder;

@Builder
public record AuthResponse(
        String token,
        String tokenType,
        Long expiresIn,
        String username,
        String correo,
        String rol,
        String mensaje
) {
}
