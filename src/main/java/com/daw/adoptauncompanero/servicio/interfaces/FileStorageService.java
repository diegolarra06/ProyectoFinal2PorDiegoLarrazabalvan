package com.daw.adoptauncompanero.servicio.interfaces;

import org.springframework.web.multipart.MultipartFile;

//PDF 6.2 - Servicio de almacenamiento de archivos
//Para subir las imágenes de los animales (2.3.2.1)
public interface FileStorageService {

 String guardarArchivo(MultipartFile archivo);

 void eliminarArchivo(String nombreArchivo);
}
