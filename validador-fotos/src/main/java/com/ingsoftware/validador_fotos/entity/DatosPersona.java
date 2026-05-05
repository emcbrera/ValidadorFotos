package com.ingsoftware.validador_fotos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_datospersona")
public class DatosPersona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dtpe_id_dtpersona_pk")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dtpe_id_usuario_fk", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "dtpe_primer_nombre", length = 50)
    private String primerNombre;

    @Column(name = "dtpe_segundo_nombre", length = 50)
    private String segundoNombre;

    @Column(name = "dtpe_primer_apellido", length = 50)
    private String primerApellido;

    @Column(name = "dtpe_segundo_apellido", length = 50)
    private String segundoApellido;

    @Column(name = "dtpe_tipo_doc", length = 5)
    private String tipoDocumento;

    @Column(name = "dtpe_numero_doc")
    private Integer numeroDocumento;

    @Column(name = "dtpe_correo", length = 50)
    private String correo;

    @Column(name = "dtpe_genero", length = 20)
    private String genero;

    @Column(name = "dtpe_celular", length = 50)
    private String celular;

    @Column(name = "dtpe_foto", length = 255)
    private String foto;

    @Column(name = "dtpe_observacion_revision", length = 500)
    private String observacionRevision;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dtpe_id_estado_fk")
    private Estado estado;
}
