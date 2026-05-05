package com.ingsoftware.validador_fotos.dto.admin;

import lombok.Builder;

@Builder
public record FotoPendienteResponse(
        Integer datosPersonaId,
        Integer usuarioId,
        String primerNombre,
        String segundoNombre,
        String primerApellido,
        String segundoApellido,
        String tipoDocumento,
        Integer numeroDocumento,
        String correo,
        String celular,
        String foto,
        String fotoUrl,
        String observacionRevision,
        String estado
) {
}
