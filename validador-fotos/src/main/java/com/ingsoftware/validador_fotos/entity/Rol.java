
package com.ingsoftware.validador_fotos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_roles")
public class Rol {

    @Id
    @Column(name = "rol_id_rol_pk")
    private Integer id;

    @Column(name = "rol_descripcion", nullable = false, unique = true, length = 150)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_estado_id_fk")
    private Estado estado;

    @Column(name = "rol_fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
}
