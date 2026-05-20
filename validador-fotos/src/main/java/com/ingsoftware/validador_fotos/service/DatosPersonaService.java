package com.ingsoftware.validador_fotos.service;

import com.ingsoftware.validador_fotos.dto.datospersona.DatosPersonaRequest;
import com.ingsoftware.validador_fotos.dto.datospersona.DatosPersonaResponse;
import com.ingsoftware.validador_fotos.entity.DatosPersona;
import com.ingsoftware.validador_fotos.entity.Estado;
import com.ingsoftware.validador_fotos.entity.Usuario;
import com.ingsoftware.validador_fotos.repository.DatosPersonaRepository;
import com.ingsoftware.validador_fotos.repository.EstadoRepository;
import com.ingsoftware.validador_fotos.repository.UsuarioRepository;
import com.ingsoftware.validador_fotos.security.CustomUserDetails;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DatosPersonaService {

    private static final Integer ESTADO_PENDIENTE_ID = 5;
    private static final String FOTO_PUBLIC_PATH = "/uploads/fotos/";

    private final DatosPersonaRepository datosPersonaRepository;
    private final EstadoRepository estadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final FotoStorageService fotoStorageService;

    public DatosPersonaResponse registrarDatosPersonales(DatosPersonaRequest request, MultipartFile foto) {
        CustomUserDetails userDetails = getAuthenticatedUser();
        Integer usuarioId = userDetails.getId();

        if (datosPersonaRepository.findByUsuarioId(usuarioId).isPresent()) {
            throw new IllegalStateException("El usuario ya tiene datos personales registrados");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalStateException("No fue posible encontrar el usuario autenticado"));
        Estado estadoPendiente = estadoRepository.findById(ESTADO_PENDIENTE_ID)
                .orElseThrow(() -> new IllegalStateException("No fue posible encontrar el estado pendiente"));
        String nombreFoto = fotoStorageService.guardarFoto(foto, usuarioId);

        DatosPersona datosPersona = new DatosPersona();
        datosPersona.setUsuario(usuario);
        datosPersona.setPrimerNombre(request.primerNombre());
        datosPersona.setSegundoNombre(request.segundoNombre());
        datosPersona.setPrimerApellido(request.primerApellido());
        datosPersona.setSegundoApellido(request.segundoApellido());
        datosPersona.setTipoDocumento(request.tipoDocumento());
        datosPersona.setNumeroDocumento(request.numeroDocumento());
        datosPersona.setCorreo(request.correo());
        datosPersona.setGenero(request.genero());
        datosPersona.setCelular(request.celular());
        datosPersona.setFoto(nombreFoto);
        datosPersona.setEstado(estadoPendiente);

        DatosPersona registroGuardado = datosPersonaRepository.save(datosPersona);

        return mapToResponse(registroGuardado, "Datos personales registrados correctamente");
    }

    public DatosPersonaResponse obtenerDatosPersonalesDelEstudiante() {
        CustomUserDetails userDetails = getAuthenticatedUser();

        DatosPersona datosPersona = datosPersonaRepository.findByUsuarioId(userDetails.getId())
                .orElseThrow(() -> new NoSuchElementException("El estudiante no tiene datos personales registrados"));

        return mapToResponse(datosPersona, "Datos personales consultados correctamente");
    }

    public DatosPersonaResponse actualizarDatosPersonales(DatosPersonaRequest request) {
        CustomUserDetails userDetails = getAuthenticatedUser();

        DatosPersona datosPersona = datosPersonaRepository.findByUsuarioId(userDetails.getId())
                .orElseThrow(() -> new NoSuchElementException("El estudiante no tiene datos personales registrados"));

        aplicarDatosFormulario(datosPersona, request);

        DatosPersona registroActualizado = datosPersonaRepository.save(datosPersona);

        return mapToResponse(registroActualizado, "Datos personales actualizados correctamente");
    }

    public DatosPersonaResponse eliminarFotoActual() {
        CustomUserDetails userDetails = getAuthenticatedUser();

        DatosPersona datosPersona = datosPersonaRepository.findByUsuarioId(userDetails.getId())
                .orElseThrow(() -> new NoSuchElementException("El estudiante no tiene datos personales registrados"));

        if (datosPersona.getFoto() == null || datosPersona.getFoto().isBlank()) {
            throw new IllegalStateException("El estudiante no tiene una fotografia registrada");
        }

        fotoStorageService.eliminarFoto(datosPersona.getFoto());
        datosPersona.setFoto(null);

        DatosPersona registroActualizado = datosPersonaRepository.save(datosPersona);

        return mapToResponse(registroActualizado, "Fotografia eliminada correctamente");
    }

    public DatosPersonaResponse reemplazarFotoActual(MultipartFile nuevaFoto) {
        CustomUserDetails userDetails = getAuthenticatedUser();

        DatosPersona datosPersona = datosPersonaRepository.findByUsuarioId(userDetails.getId())
                .orElseThrow(() -> new NoSuchElementException("El estudiante no tiene datos personales registrados"));

        if (datosPersona.getFoto() != null && !datosPersona.getFoto().isBlank()) {
            fotoStorageService.eliminarFoto(datosPersona.getFoto());
        }

        String nombreNuevaFoto = fotoStorageService.guardarFoto(nuevaFoto, userDetails.getId());
        datosPersona.setFoto(nombreNuevaFoto);
        
        Estado estadoPendiente = estadoRepository.findById(ESTADO_PENDIENTE_ID)
                .orElseThrow(() -> new IllegalStateException("No fue posible encontrar el estado pendiente"));
        datosPersona.setEstado(estadoPendiente);
        datosPersona.setObservacionRevision(null);

        DatosPersona registroActualizado = datosPersonaRepository.save(datosPersona);

        return mapToResponse(registroActualizado, "Fotografia actualizada correctamente");
    }

    private String construirFotoUrl(String nombreFoto) {
        if (nombreFoto == null || nombreFoto.isBlank()) {
            return null;
        }

        return FOTO_PUBLIC_PATH + nombreFoto;
    }

    private void aplicarDatosFormulario(DatosPersona datosPersona, DatosPersonaRequest request) {
        datosPersona.setPrimerNombre(request.primerNombre());
        datosPersona.setSegundoNombre(request.segundoNombre());
        datosPersona.setPrimerApellido(request.primerApellido());
        datosPersona.setSegundoApellido(request.segundoApellido());
        datosPersona.setTipoDocumento(request.tipoDocumento());
        datosPersona.setNumeroDocumento(request.numeroDocumento());
        datosPersona.setCorreo(request.correo());
        datosPersona.setGenero(request.genero());
        datosPersona.setCelular(request.celular());
    }

    private DatosPersonaResponse mapToResponse(DatosPersona datosPersona, String mensaje) {
        return DatosPersonaResponse.builder()
                .id(datosPersona.getId())
                .usuarioId(datosPersona.getUsuario() != null ? datosPersona.getUsuario().getId() : null)
                .primerNombre(datosPersona.getPrimerNombre())
                .segundoNombre(datosPersona.getSegundoNombre())
                .primerApellido(datosPersona.getPrimerApellido())
                .segundoApellido(datosPersona.getSegundoApellido())
                .tipoDocumento(datosPersona.getTipoDocumento())
                .numeroDocumento(datosPersona.getNumeroDocumento())
                .correo(datosPersona.getCorreo())
                .genero(datosPersona.getGenero())
                .celular(datosPersona.getCelular())
                .foto(datosPersona.getFoto())
                .fotoUrl(construirFotoUrl(datosPersona.getFoto()))
                .observacionRevision(datosPersona.getObservacionRevision())
                .estado(datosPersona.getEstado() != null ? datosPersona.getEstado().getDescripcion() : null)
                .mensaje(mensaje)
                .build();
    }

    private CustomUserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new IllegalStateException("No hay un usuario autenticado en la solicitud");
        }

        return userDetails;
    }
}
