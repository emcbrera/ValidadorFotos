package com.ingsoftware.validador_fotos.controller;

import com.ingsoftware.validador_fotos.dto.admin.FotoPendienteResponse;
import com.ingsoftware.validador_fotos.service.AdminFotoRevisionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
