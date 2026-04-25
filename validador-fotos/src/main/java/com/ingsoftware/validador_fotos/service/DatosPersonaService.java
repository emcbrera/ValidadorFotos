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
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DatosPersonaService {

    private static final Integer ESTADO_PENDIENTE_ID = 5;

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

        return DatosPersonaResponse.builder()
                .id(registroGuardado.getId())
                .usuarioId(usuario.getId())
                .primerNombre(registroGuardado.getPrimerNombre())
                .segundoNombre(registroGuardado.getSegundoNombre())
                .primerApellido(registroGuardado.getPrimerApellido())
                .segundoApellido(registroGuardado.getSegundoApellido())
                .tipoDocumento(registroGuardado.getTipoDocumento())
                .numeroDocumento(registroGuardado.getNumeroDocumento())
                .correo(registroGuardado.getCorreo())
                .genero(registroGuardado.getGenero())
                .celular(registroGuardado.getCelular())
                .foto(registroGuardado.getFoto())
                .estado(registroGuardado.getEstado() != null ? registroGuardado.getEstado().getDescripcion() : null)
                .mensaje("Datos personales registrados correctamente")
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
