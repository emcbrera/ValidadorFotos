package com.ingsoftware.validador_fotos.controller;

import com.ingsoftware.validador_fotos.dto.admin.FotoPendienteResponse;
import com.ingsoftware.validador_fotos.dto.admin.RechazarFotoRequest;
import com.ingsoftware.validador_fotos.service.AdminFotoRevisionService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/fotos")
@RequiredArgsConstructor
public class AdminFotoRevisionController {

    private final AdminFotoRevisionService adminFotoRevisionService;

    @GetMapping("/pendientes")
    public ResponseEntity<List<FotoPendienteResponse>> listarFotosPendientes() {
        return ResponseEntity.ok(adminFotoRevisionService.listarFotosPendientes());
    }

    @GetMapping("/pendientes/{datosPersonaId}")
    public ResponseEntity<FotoPendienteResponse> obtenerDetalleFotoPendiente(
            @PathVariable Integer datosPersonaId) {
        return ResponseEntity.ok(adminFotoRevisionService.obtenerDetalleFotoPendiente(datosPersonaId));
    }

    @PutMapping("/{datosPersonaId}/aprobar")
    public ResponseEntity<FotoPendienteResponse> aprobarFoto(@PathVariable Integer datosPersonaId) {
        return ResponseEntity.ok(adminFotoRevisionService.aprobarFoto(datosPersonaId));
    }

    @PutMapping("/{datosPersonaId}/rechazar")
    public ResponseEntity<FotoPendienteResponse> rechazarFoto(
            @PathVariable Integer datosPersonaId,
            @Valid @RequestBody RechazarFotoRequest request) {
        return ResponseEntity.ok(adminFotoRevisionService.rechazarFoto(datosPersonaId, request.observacion()));
    }
}
