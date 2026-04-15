
package com.ingsoftware.validador_fotos.security;

import com.ingsoftware.validador_fotos.entity.Usuario;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String rol = usuario.getRol() != null ? usuario.getRol().getDescripcion() : "SIN_ROL";
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase().replace(" ", "_")));
    }

    @Override
    public String getPassword() {
        return usuario.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return usuario.getUsername();
    }

    public String getCorreo() {
        return usuario.getCorreo();
    }

    public String getRol() {
        return usuario.getRol() != null ? usuario.getRol().getDescripcion() : null;
    }

    public Integer getId() {
        return usuario.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        String estado = usuario.getEstado() != null ? usuario.getEstado().getDescripcion() : null;
        return estado == null || "ACTIVO".equalsIgnoreCase(estado);
    }
}
