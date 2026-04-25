package com.ingsoftware.validador_fotos.repository;

import com.ingsoftware.validador_fotos.entity.DatosPersona;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatosPersonaRepository extends JpaRepository<DatosPersona, Integer> {

    Optional<DatosPersona> findByUsuarioId(Integer usuarioId);
}
