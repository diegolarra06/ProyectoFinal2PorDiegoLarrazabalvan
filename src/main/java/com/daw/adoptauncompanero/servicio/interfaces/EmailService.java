package com.daw.adoptauncompanero.servicio.interfaces;

//PDF 6.4 - Envío de emails
//Notificaciones automáticas al cambiar el estado de una solicitud (2.2.5 / 2.4.7)
public interface EmailService {

 void enviarEmailSimple(String para, String asunto, String cuerpo);

 void enviarEmailHtml(String para, String asunto, String htmlCuerpo);

 // Email automático cuando cambia el estado de una solicitud
 void notificarCambioEstadoSolicitud(String para, String nombreUsuario,
                                     String nombreAnimal, String estadoAnterior,
                                     String estadoNuevo, String comentario);

 // Email automático cuando se programa una cita
 void notificarCitaProgramada(String para, String nombreUsuario, String nombreAnimal,
                              String fechaCita, String observaciones);
}