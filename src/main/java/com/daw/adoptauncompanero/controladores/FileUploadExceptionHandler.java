package com.daw.adoptauncompanero.controladores;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// =============================================================
// PDF 6.2 - 3.3. Captura del límite de tamaño en subida de archivos
// =============================================================
@ControllerAdvice
public class FileUploadExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error",
                "El archivo supera el tamaño máximo permitido (5 MB).");
        return "redirect:/animales/catalogo";
    }
}
