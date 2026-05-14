package com.ingsoftware.validador_fotos.service;

import com.ingsoftware.validador_fotos.dto.admin.FotoPendienteResponse;
import com.ingsoftware.validador_fotos.entity.DatosPersona;
import com.ingsoftware.validador_fotos.entity.Estado;
import com.ingsoftware.validador_fotos.repository.DatosPersonaRepository;
import com.ingsoftware.validador_fotos.repository.EstadoRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminFotoRevisionService {

    private static final Integer ESTADO_APROBADA_ID = 1;
    private static final Integer ESTADO_RECHAZADA_ID = 2;
    private static final Integer ESTADO_PENDIENTE_ID = 5;
    private static final String ROL_ESTUDIANTE = "ESTUDIANTE";
    private static final String FOTO_PUBLIC_PATH = "/uploads/fotos/";

    private final DatosPersonaRepository datosPersonaRepository;
    private final EstadoRepository estadoRepository;

    public List<FotoPendienteResponse> listarFotosPendientes() {
        return datosPersonaRepository
                .findAllByOrderByPrimerApellidoAscPrimerNombreAsc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public FotoPendienteResponse obtenerDetalleFotoPendiente(Integer datosPersonaId) {
        return mapToResponse(obtenerFotoPendiente(datosPersonaId));
    }

    public FotoPendienteResponse aprobarFoto(Integer datosPersonaId) {
        DatosPersona datosPersona = obtenerFotoPendiente(datosPersonaId);
        Estado estadoAprobada = obtenerEstado(ESTADO_APROBADA_ID, "aprobada");

        datosPersona.setEstado(estadoAprobada);
        datosPersona.setObservacionRevision(null);

        return mapToResponse(datosPersonaRepository.save(datosPersona));
    }

    public FotoPendienteResponse rechazarFoto(Integer datosPersonaId, String observacion) {
        DatosPersona datosPersona = obtenerFotoPendiente(datosPersonaId);
        Estado estadoRechazada = obtenerEstado(ESTADO_RECHAZADA_ID, "rechazada");

        datosPersona.setEstado(estadoRechazada);
        datosPersona.setObservacionRevision(observacion.trim());

        return mapToResponse(datosPersonaRepository.save(datosPersona));
    }

    private DatosPersona obtenerFotoPendiente(Integer datosPersonaId) {
        return datosPersonaRepository
                .findByIdAndEstadoIdAndFotoIsNotNullAndUsuarioRolDescripcionIgnoreCase(
                        datosPersonaId,
                        ESTADO_PENDIENTE_ID,
                        ROL_ESTUDIANTE
                )
                .orElseThrow(() -> new NoSuchElementException("No se encontro una foto pendiente para el estudiante indicado"));
    }

    private Estado obtenerEstado(Integer estadoId, String descripcion) {
        return estadoRepository.findById(estadoId)
                .orElseThrow(() -> new IllegalStateException("No fue posible encontrar el estado " + descripcion));
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
                .observacionRevision(datosPersona.getObservacionRevision())
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
