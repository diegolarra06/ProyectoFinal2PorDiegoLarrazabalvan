package com.daw.adoptauncompanero.servicio.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.daw.adoptauncompanero.servicio.interfaces.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender mailSender;

	@Value("${app.mail.from}")
	private String from;

	public EmailServiceImpl(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public void enviarEmailSimple(String para, String asunto, String cuerpo) {
		SimpleMailMessage mensaje = new SimpleMailMessage();
		mensaje.setFrom(from);
		mensaje.setTo(para);
		mensaje.setSubject(asunto);
		mensaje.setText(cuerpo);
		mailSender.send(mensaje);
		System.out.println("Email simple enviado a: " + para);
	}

	@Override
	public void enviarEmailHtml(String para, String asunto, String htmlCuerpo) {
		try {
			MimeMessage mensaje = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
			helper.setFrom(from);
			helper.setTo(para);
			helper.setSubject(asunto);
			helper.setText(htmlCuerpo, true);
			mailSender.send(mensaje);
			System.out.println("Email HTML enviado a: " + para);
		} catch (MessagingException e) {
			throw new RuntimeException("Error al enviar email HTML", e);
		}
	}

	@Override
	public void notificarCambioEstadoSolicitud(String para, String nombreUsuario, String nombreAnimal,
			String estadoAnterior, String estadoNuevo, String comentario) {
		String html = """
				<html>
				  <body style='font-family: Arial, sans-serif;'>
				    <h2 style='color:#2e7d32;'>AdoptaUnCompañero</h2>
				    <p>Hola <b>%s</b>,</p>
				    <p>Tu solicitud de adopción para <b>%s</b> ha cambiado de estado:</p>
				    <p>
				      <span style='color:#888;'>%s</span>
				      &nbsp;&rarr;&nbsp;
				      <span style='color:#1976d2; font-weight:bold;'>%s</span>
				    </p>
				    <p><i>Comentario del centro:</i> %s</p>
				    <hr>
				    <p style='font-size:12px; color:#666;'>
				      Puedes consultar el estado de tu solicitud en tu área personal.
				    </p>
				  </body>
				</html>
				""".formatted(nombreUsuario, nombreAnimal, estadoAnterior == null ? "INICIO" : estadoAnterior,
				estadoNuevo, comentario == null ? "—" : comentario);

		enviarEmailHtml(para, "Actualización de tu solicitud de adopción", html);
	}

	@Override
	public void notificarCitaProgramada(String para, String nombreUsuario, String nombreAnimal, String fechaCita,
			String observaciones) {
		String html = """
				<html>
				  <body style='font-family: Arial, sans-serif;'>
				    <h2 style='color:#2e7d32;'>AdoptaUnCompañero</h2>
				    <p>Hola <b>%s</b>,</p>
				    <p>Se ha programado una cita para tu proceso de adopción de <b>%s</b>.</p>
				    <p><b>Fecha:</b> %s</p>
				    <p><b>Observaciones:</b> %s</p>
				    <hr>
				    <p style='font-size:12px; color:#666;'>Te esperamos en el centro.</p>
				  </body>
				</html>
				""".formatted(nombreUsuario, nombreAnimal, fechaCita, observaciones == null ? "—" : observaciones);

		enviarEmailHtml(para, "Cita programada para tu adopción", html);
	}
}