package com.ingsoftware.validador_fotos.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoStorageService {

    private static final Set<String> EXTENSIONES_PERMITIDAS = Set.of("jpg", "jpeg", "png");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final Path storagePath;
    private final Set<String> allowedContentTypes;

    public FotoStorageService(
            @Value("${app.storage.fotos.path}") String storagePath,
            @Value("${app.storage.fotos.allowed-types}") String allowedTypes) {
        this.storagePath = Paths.get(storagePath).toAbsolutePath().normalize();
        this.allowedContentTypes = Arrays.stream(allowedTypes.split(","))
                .map(String::trim)
                .filter(type -> !type.isBlank())
                .collect(Collectors.toCollection(HashSet::new));
    }

    public String guardarFoto(MultipartFile archivo, Integer usuarioId) {
        validarArchivo(archivo);

        try {
            Files.createDirectories(storagePath);

            String extension = obtenerExtension(archivo.getOriginalFilename());
            String nombreArchivo = generarNombreArchivo(usuarioId, extension);
            Path destino = storagePath.resolve(nombreArchivo);

            try (InputStream inputStream = archivo.getInputStream()) {
                Files.copy(inputStream, destino, StandardCopyOption.REPLACE_EXISTING);
            }

            return nombreArchivo;
        } catch (IOException ex) {
            throw new IllegalStateException("No fue posible almacenar la fotografia", ex);
        }
    }

    public void eliminarFoto(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.isBlank()) {
            return;
        }

        try {
            Path archivo = storagePath.resolve(nombreArchivo).normalize();
            Files.deleteIfExists(archivo);
        } catch (IOException ex) {
            throw new IllegalStateException("No fue posible eliminar la fotografia almacenada", ex);
        }
    }

    private void validarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalStateException("La fotografia es obligatoria");
        }

        String contentType = archivo.getContentType();
        if (contentType == null || !allowedContentTypes.contains(contentType)) {
            throw new IllegalStateException("El tipo de archivo no es permitido. Solo se aceptan imagenes JPG o PNG");
        }

        String extension = obtenerExtension(archivo.getOriginalFilename());
        if (!EXTENSIONES_PERMITIDAS.contains(extension.toLowerCase())) {
            throw new IllegalStateException("La extension del archivo no es valida");
        }
    }

    private String generarNombreArchivo(Integer usuarioId, String extension) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8);
        return "usuario_" + usuarioId + "_" + timestamp + "_" + randomSuffix + "." + extension.toLowerCase();
    }

    private String obtenerExtension(String nombreOriginal) {
        if (nombreOriginal == null || !nombreOriginal.contains(".")) {
            throw new IllegalStateException("No fue posible identificar la extension del archivo");
        }

        String nombreLimpio = Normalizer.normalize(nombreOriginal, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");

        return nombreLimpio.substring(nombreLimpio.lastIndexOf('.') + 1);
    }
}
