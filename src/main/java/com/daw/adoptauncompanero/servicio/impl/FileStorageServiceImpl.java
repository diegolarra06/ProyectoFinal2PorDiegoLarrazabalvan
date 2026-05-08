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

@Service
public class FileStorageServiceImpl implements FileStorageService {

	private final Path carpetaAlmacenamiento;

	public FileStorageServiceImpl(@Value("${app.upload.dir}") String uploadDir) {
		this.carpetaAlmacenamiento = Paths.get(uploadDir).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.carpetaAlmacenamiento);
		} catch (IOException e) {
			throw new RuntimeException("No se pudo crear la carpeta de uploads: " + uploadDir, e);
		}
	}

	@Override
	public String guardarArchivo(MultipartFile archivo) {

		if (archivo == null || archivo.isEmpty()) {
			throw new IllegalArgumentException("El archivo está vacío o no se ha seleccionado.");
		}

		String contentType = archivo.getContentType();
		if (contentType == null || !contentType.startsWith("image/")) {
			throw new IllegalArgumentException("Solo se permiten archivos de imagen.");
		}

		String nombreOriginal = archivo.getOriginalFilename();
		String extension = "";
		if (nombreOriginal != null && nombreOriginal.contains(".")) {
			extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
		}

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
			System.err.println("Error al eliminar archivo " + nombreArchivo + ": " + e.getMessage());
		}
	}
}
