package com.ingsoftware.validador_fotos.controller;

import com.ingsoftware.validador_fotos.dto.datospersona.DatosPersonaRequest;
import com.ingsoftware.validador_fotos.dto.datospersona.DatosPersonaResponse;
import com.ingsoftware.validador_fotos.service.DatosPersonaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/estudiante/datos-personales")
@RequiredArgsConstructor
public class DatosPersonaController {

    private final DatosPersonaService datosPersonaService;

    @GetMapping
    public ResponseEntity<DatosPersonaResponse> obtenerDatosPersonalesDelEstudiante() {
        return ResponseEntity.ok(datosPersonaService.obtenerDatosPersonalesDelEstudiante());
    }

    @DeleteMapping("/foto")
    public ResponseEntity<DatosPersonaResponse> eliminarFotoActual() {
        return ResponseEntity.ok(datosPersonaService.eliminarFotoActual());
    }

    @PutMapping("/foto")
    public ResponseEntity<DatosPersonaResponse> reemplazarFotoActual(
            @RequestParam MultipartFile foto) {
        return ResponseEntity.ok(datosPersonaService.reemplazarFotoActual(foto));
    }

    @PutMapping
    public ResponseEntity<DatosPersonaResponse> actualizarDatosPersonales(
            @Valid @RequestBody DatosPersonaRequest request) {
        return ResponseEntity.ok(datosPersonaService.actualizarDatosPersonales(request));
    }

    @PostMapping
    public ResponseEntity<DatosPersonaResponse> registrarDatosPersonales(
            @RequestParam String primerNombre,
            @RequestParam(required = false) String segundoNombre,
            @RequestParam String primerApellido,
            @RequestParam(required = false) String segundoApellido,
            @RequestParam String tipoDocumento,
            @RequestParam Integer numeroDocumento,
            @RequestParam String correo,
            @RequestParam String genero,
            @RequestParam String celular,
            @RequestParam MultipartFile foto) {
        DatosPersonaRequest request = DatosPersonaRequest.fromFormData(
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                tipoDocumento,
                numeroDocumento,
                correo,
                genero,
                celular
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(datosPersonaService.registrarDatosPersonales(request, foto));
    }
}
