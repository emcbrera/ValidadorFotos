
package com.ingsoftware.validador_fotos.controller;

import com.ingsoftware.validador_fotos.dto.auth.AuthResponse;
import com.ingsoftware.validador_fotos.dto.auth.ForgotPasswordRequest;
import com.ingsoftware.validador_fotos.dto.auth.ForgotPasswordResponse;
import com.ingsoftware.validador_fotos.dto.auth.LoginRequest;
import com.ingsoftware.validador_fotos.dto.auth.ResetPasswordRequest;
import com.ingsoftware.validador_fotos.dto.auth.ResetPasswordResponse;
import com.ingsoftware.validador_fotos.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(authService.requestPasswordRecovery(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(authService.resetPassword(request));
    }
}
