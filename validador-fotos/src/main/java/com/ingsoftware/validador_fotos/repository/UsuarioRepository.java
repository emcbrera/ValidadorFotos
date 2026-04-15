
package com.ingsoftware.validador_fotos.repository;

import com.ingsoftware.validador_fotos.entity.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByUsernameIgnoreCase(String username);

    Optional<Usuario> findByCorreoIgnoreCase(String correo);

    Optional<Usuario> findByUsernameIgnoreCaseOrCorreoIgnoreCase(String username, String correo);
}
