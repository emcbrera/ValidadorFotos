package com.ingsoftware.validador_fotos.controller;

import com.ingsoftware.validador_fotos.dto.datospersona.DatosPersonaRequest;
import com.ingsoftware.validador_fotos.dto.datospersona.DatosPersonaResponse;
import com.ingsoftware.validador_fotos.service.DatosPersonaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estudiante/datos-personales")
@RequiredArgsConstructor
public class DatosPersonaController {

    private final DatosPersonaService datosPersonaService;

    @PostMapping
    public ResponseEntity<DatosPersonaResponse> registrarDatosPersonales(
            @Valid @RequestBody DatosPersonaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(datosPersonaService.registrarDatosPersonales(request));
    }
}
