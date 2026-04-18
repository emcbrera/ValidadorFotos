
package com.ingsoftware.validador_fotos.service;

import com.ingsoftware.validador_fotos.dto.auth.AuthResponse;
import com.ingsoftware.validador_fotos.dto.auth.ForgotPasswordRequest;
import com.ingsoftware.validador_fotos.dto.auth.ForgotPasswordResponse;
import com.ingsoftware.validador_fotos.dto.auth.LoginRequest;
import com.ingsoftware.validador_fotos.dto.auth.ResetPasswordRequest;
import com.ingsoftware.validador_fotos.dto.auth.ResetPasswordResponse;
import com.ingsoftware.validador_fotos.entity.Usuario;
import com.ingsoftware.validador_fotos.repository.UsuarioRepository;
import com.ingsoftware.validador_fotos.security.CustomUserDetails;
import com.ingsoftware.validador_fotos.security.JwtService;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${app.frontend.reset-url}")
    private String resetPasswordUrl;

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

    public ForgotPasswordResponse requestPasswordRecovery(ForgotPasswordRequest request) {
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(request.correo())
                .orElseThrow(() -> new BadCredentialsException("No existe un usuario registrado con ese correo"));

        String token = UUID.randomUUID().toString();
        LocalDateTime expiracion = LocalDateTime.now().plusMinutes(30);

        usuario.setTokenRecuperacion(token);
        usuario.setTokenExpiracion(expiracion);
        usuarioRepository.save(usuario);

        String enlaceRecuperacion = resetPasswordUrl + "?token=" + token;
        String cuerpoCorreo = """
                Hola,

                Recibimos una solicitud para restablecer tu contrasena en Validador Fotos.

                Usa el siguiente enlace para crear una nueva contrasena:
                %s

                Este enlace expirara en 30 minutos.

                Si no solicitaste este cambio, puedes ignorar este mensaje.
                """.formatted(enlaceRecuperacion);

        emailService.sendSimpleEmail(
                usuario.getCorreo(),
                "Recuperacion de contrasena - Validador Fotos",
                cuerpoCorreo);

        return ForgotPasswordResponse.builder()
                .mensaje("Se envio un enlace de recuperacion al correo registrado")
                .correo(usuario.getCorreo())
                .build();
    }

    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {
        Usuario usuario = usuarioRepository.findByTokenRecuperacion(request.token())
                .orElseThrow(() -> new BadCredentialsException("El token de recuperacion no es valido"));

        if (usuario.getTokenExpiracion() == null || usuario.getTokenExpiracion().isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("El token de recuperacion ha expirado");
        }

        usuario.setPasswordHash(passwordEncoder.encode(request.nuevaPassword()));
        usuario.setTokenRecuperacion(null);
        usuario.setTokenExpiracion(null);
        usuarioRepository.save(usuario);

        return ResetPasswordResponse.builder()
                .mensaje("Contrasena restablecida correctamente")
                .correo(usuario.getCorreo())
                .build();
    }
}
