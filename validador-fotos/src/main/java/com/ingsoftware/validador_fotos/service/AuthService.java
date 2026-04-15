
package com.ingsoftware.validador_fotos.service;

import com.ingsoftware.validador_fotos.dto.auth.AuthResponse;
import com.ingsoftware.validador_fotos.dto.auth.LoginRequest;
import com.ingsoftware.validador_fotos.security.CustomUserDetails;
import com.ingsoftware.validador_fotos.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse authenticate(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.identificador(), request.password()));

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            return AuthResponse.builder()
                    .token(token)
                    .tokenType("Bearer")
                    .expiresIn(jwtService.getJwtExpirationMs() / 1000)
                    .username(userDetails.getUsername())
                    .correo(userDetails.getCorreo())
                    .rol(userDetails.getRol())
                    .mensaje("Autenticacion exitosa")
                    .build();
        } catch (DisabledException ex) {
            throw new BadCredentialsException("El usuario no esta activo");
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Credenciales invalidas");
        }
    }
}
