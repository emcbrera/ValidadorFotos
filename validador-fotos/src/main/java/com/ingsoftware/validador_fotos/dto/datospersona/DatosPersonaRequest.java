package com.ingsoftware.validador_fotos.dto.datospersona;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DatosPersonaRequest(
        @NotBlank(message = "El primer nombre es obligatorio")
        @Size(max = 50, message = "El primer nombre no puede superar los 50 caracteres")
        String primerNombre,

        @Size(max = 50, message = "El segundo nombre no puede superar los 50 caracteres")
        String segundoNombre,

        @NotBlank(message = "El primer apellido es obligatorio")
        @Size(max = 50, message = "El primer apellido no puede superar los 50 caracteres")
        String primerApellido,

        @Size(max = 50, message = "El segundo apellido no puede superar los 50 caracteres")
        String segundoApellido,

        @NotBlank(message = "El tipo de documento es obligatorio")
        @Size(max = 5, message = "El tipo de documento no puede superar los 5 caracteres")
        String tipoDocumento,

        @NotNull(message = "El numero de documento es obligatorio")
        Integer numeroDocumento,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato valido")
        @Size(max = 50, message = "El correo no puede superar los 50 caracteres")
        String correo,

        @NotBlank(message = "El genero es obligatorio")
        @Size(max = 20, message = "El genero no puede superar los 20 caracteres")
        String genero,

        @NotBlank(message = "El celular es obligatorio")
        @Size(max = 50, message = "El celular no puede superar los 50 caracteres")
        @Pattern(regexp = "^[0-9+\\-\\s]+$", message = "El celular contiene caracteres no permitidos")
        String celular
) {
    public static DatosPersonaRequest fromFormData(
            String primerNombre,
            String segundoNombre,
            String primerApellido,
            String segundoApellido,
            String tipoDocumento,
            Integer numeroDocumento,
            String correo,
            String genero,
            String celular) {
        return new DatosPersonaRequest(
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                tipoDocumento,
                numeroDocumento,
                correo,
                genero,
                celular
        );
    }
}
