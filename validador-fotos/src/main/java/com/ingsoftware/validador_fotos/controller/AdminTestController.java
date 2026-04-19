package com.ingsoftware.validador_fotos.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminTestController {

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test(Authentication authentication) {
        return ResponseEntity.ok(Map.of(
                "mensaje", "Acceso permitido para administrador",
                "usuario", authentication.getName()));
    }
}
