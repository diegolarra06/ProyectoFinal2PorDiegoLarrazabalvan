package com.daw.adoptauncompanero.servicio.interfaces;

public interface EmailService {

	void enviarEmailSimple(String para, String asunto, String cuerpo);

	void enviarEmailHtml(String para, String asunto, String htmlCuerpo);

	void notificarCambioEstadoSolicitud(String para, String nombreUsuario, String nombreAnimal, String estadoAnterior,
			String estadoNuevo, String comentario);

	void notificarCitaProgramada(String para, String nombreUsuario, String nombreAnimal, String fechaCita,
			String observaciones);
}