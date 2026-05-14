package com.ingsoftware.validador_fotos.dto.datospersona;

import lombok.Builder;

@Builder
public record DatosPersonaResponse(
        Integer id,
        Integer usuarioId,
        String primerNombre,
        String segundoNombre,
        String primerApellido,
        String segundoApellido,
        String tipoDocumento,
        Integer numeroDocumento,
        String correo,
        String genero,
        String celular,
        String foto,
        String fotoUrl,
        String observacionRevision,
        String estado,
        String mensaje
) {
}
