package com.daw.adoptauncompanero.servicio.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.daw.adoptauncompanero.servicio.interfaces.FileStorageService;

// =============================================================
// PDF 6.2 - 3.2. FileStorageService
// Guarda las imágenes de los animales con un nombre único (UUID)
// para evitar colisiones y ataques de path traversal.
// =============================================================
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path carpetaAlmacenamiento;

    public FileStorageServiceImpl(@Value("${app.upload.dir}") String uploadDir) {
        this.carpetaAlmacenamiento = Paths.get(uploadDir).toAbsolutePath().normalize();

        // Crear la carpeta si no existe
        try {
            Files.createDirectories(this.carpetaAlmacenamiento);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear la carpeta de uploads: " + uploadDir, e);
        }
    }

    @Override
    public String guardarArchivo(MultipartFile archivo) {

        // Validación: viene algún archivo?
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío o no se ha seleccionado.");
        }

        // Validación: solo permitimos imágenes
        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Solo se permiten archivos de imagen.");
        }

        // Obtenemos extensión
        String nombreOriginal = archivo.getOriginalFilename();
        String extension = "";
        if (nombreOriginal != null && nombreOriginal.contains(".")) {
            extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        }

        // Nombre único para evitar colisiones (PDF 6.2 - 1.5)
        String nombreUnico = UUID.randomUUID().toString() + extension;

        try {
            Path destino = this.carpetaAlmacenamiento.resolve(nombreUnico);
            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo: " + nombreOriginal, e);
        }

        return nombreUnico;
    }

    @Override
    public void eliminarArchivo(String nombreArchivo) {
        try {
            Path archivo = this.carpetaAlmacenamiento.resolve(nombreArchivo);
            Files.deleteIfExists(archivo);
        } catch (IOException e) {
            // Lo logueamos, pero no rompemos el flujo
            System.err.println("Error al eliminar archivo " + nombreArchivo + ": " + e.getMessage());
        }
    }
}
