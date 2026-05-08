package com.daw.adoptauncompanero.servicio.interfaces;

import org.springframework.web.multipart.MultipartFile;


public interface FileStorageService {

 String guardarArchivo(MultipartFile archivo);

 void eliminarArchivo(String nombreArchivo);
}
