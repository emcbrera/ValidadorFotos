
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
@Table(name = "tb_usuarios")
public class Usuario {

    @Id
    @Column(name = "usua_id_usuario_pk")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usua_id_rol_fk")
    private Rol rol;

    @Column(name = "usua_username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "usua_correo", nullable = false, unique = true, length = 100)
    private String correo;

    @Column(name = "usua_password_hash", nullable = false)
    private String passwordHash;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usua_id_estado_fk")
    private Estado estado;

    @Column(name = "usua_fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
}
