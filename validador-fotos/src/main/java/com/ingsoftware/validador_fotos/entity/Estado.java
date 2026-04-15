
package com.ingsoftware.validador_fotos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_estados")
public class Estado {

    @Id
    @Column(name = "esta_id_estado_pk")
    private Integer id;

    @Column(name = "esta_descripcion", nullable = false, unique = true, length = 150)
    private String descripcion;

    @Column(name = "esta_fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
}
