package com.ingsoftware.validador_fotos.service;

import com.ingsoftware.validador_fotos.dto.admin.FotoPendienteResponse;
import com.ingsoftware.validador_fotos.entity.DatosPersona;
import com.ingsoftware.validador_fotos.repository.DatosPersonaRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminFotoRevisionService {

    private static final Integer ESTADO_PENDIENTE_ID = 5;
    private static final String ROL_ESTUDIANTE = "ESTUDIANTE";
    private static final String FOTO_PUBLIC_PATH = "/uploads/fotos/";

    private final DatosPersonaRepository datosPersonaRepository;

    public List<FotoPendienteResponse> listarFotosPendientes() {
        return datosPersonaRepository
                .findByEstadoIdAndFotoIsNotNullAndUsuarioRolDescripcionIgnoreCaseOrderByPrimerApellidoAscPrimerNombreAsc(
                        ESTADO_PENDIENTE_ID,
                        ROL_ESTUDIANTE
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public FotoPendienteResponse obtenerDetalleFotoPendiente(Integer datosPersonaId) {
        DatosPersona datosPersona = datosPersonaRepository
                .findByIdAndEstadoIdAndFotoIsNotNullAndUsuarioRolDescripcionIgnoreCase(
                        datosPersonaId,
                        ESTADO_PENDIENTE_ID,
                        ROL_ESTUDIANTE
                )
                .orElseThrow(() -> new NoSuchElementException("No se encontro una foto pendiente para el estudiante indicado"));

        return mapToResponse(datosPersona);
    }

    private FotoPendienteResponse mapToResponse(DatosPersona datosPersona) {
        return FotoPendienteResponse.builder()
                .datosPersonaId(datosPersona.getId())
                .usuarioId(datosPersona.getUsuario() != null ? datosPersona.getUsuario().getId() : null)
                .primerNombre(datosPersona.getPrimerNombre())
                .segundoNombre(datosPersona.getSegundoNombre())
                .primerApellido(datosPersona.getPrimerApellido())
                .segundoApellido(datosPersona.getSegundoApellido())
                .tipoDocumento(datosPersona.getTipoDocumento())
                .numeroDocumento(datosPersona.getNumeroDocumento())
                .correo(datosPersona.getCorreo())
                .celular(datosPersona.getCelular())
                .foto(datosPersona.getFoto())
                .fotoUrl(construirFotoUrl(datosPersona.getFoto()))
                .estado(datosPersona.getEstado() != null ? datosPersona.getEstado().getDescripcion() : null)
                .build();
    }

    private String construirFotoUrl(String nombreFoto) {
        if (nombreFoto == null || nombreFoto.isBlank()) {
            return null;
        }

        return FOTO_PUBLIC_PATH + nombreFoto;
    }
}
