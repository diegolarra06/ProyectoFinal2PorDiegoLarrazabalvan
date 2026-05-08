package com.daw.adoptauncompanero.servicio.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.adoptauncompanero.dtos.HistorialEstadoDTO;
import com.daw.adoptauncompanero.dtos.SolicitudDTO;
import com.daw.adoptauncompanero.entities.AnimalEntity;
import com.daw.adoptauncompanero.entities.EstadoSolicitudEntity;
import com.daw.adoptauncompanero.entities.HistorialEstadoSolicitudEntity;
import com.daw.adoptauncompanero.entities.NotificacionEntity;
import com.daw.adoptauncompanero.entities.SolicitudAdopcionEntity;
import com.daw.adoptauncompanero.entities.UsuarioEntity;
import com.daw.adoptauncompanero.repositorios.AnimalRepository;
import com.daw.adoptauncompanero.repositorios.EstadoSolicitudRepository;
import com.daw.adoptauncompanero.repositorios.HistorialEstadoRepository;
import com.daw.adoptauncompanero.repositorios.NotificacionRepository;
import com.daw.adoptauncompanero.repositorios.SolicitudAdopcionRepository;
import com.daw.adoptauncompanero.repositorios.UsuarioRepository;
import com.daw.adoptauncompanero.servicio.interfaces.EmailService;
import com.daw.adoptauncompanero.servicio.interfaces.SolicitudService;

@Service
public class SolicitudServiceImpl implements SolicitudService {

	@Autowired
	private SolicitudAdopcionRepository solicitudRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private AnimalRepository animalRepository;
	@Autowired
	private EstadoSolicitudRepository estadoRepository;
	@Autowired
	private HistorialEstadoRepository historialRepository;
	@Autowired
	private NotificacionRepository notificacionRepository;
	@Autowired
	private EmailService emailService;

	@Override
	@Transactional
	public Integer iniciarSolicitud(Integer idUsuario, Integer idAnimal, String comentarios) {

		UsuarioEntity usuario = usuarioRepository.findById(idUsuario).orElse(null);
		AnimalEntity animal = animalRepository.findById(idAnimal).orElse(null);
		if (usuario == null || animal == null)
			return 0;

		if (animal.getEstado() == AnimalEntity.EstadoAnimal.ADOPTADO)
			return -1;

		if (solicitudRepository.contarSolicitudesUsuarioAnimal(idUsuario, idAnimal) > 0)
			return -2;

		EstadoSolicitudEntity estadoInicial = estadoRepository.findByNombre("EN_REVISION");

		SolicitudAdopcionEntity s = new SolicitudAdopcionEntity();
		s.setUsuario(usuario);
		s.setAnimal(animal);
		s.setEstado(estadoInicial);
		s.setComentarios(comentarios);
		s.setFechaSolicitud(LocalDateTime.now());

		Integer idGenerado = solicitudRepository.save(s).getIdSolicitud();

		HistorialEstadoSolicitudEntity h = new HistorialEstadoSolicitudEntity(s, null, estadoInicial,
				LocalDateTime.now(), "Solicitud creada");
		historialRepository.save(h);

		return idGenerado;
	}

	@Override
	public List<SolicitudDTO> buscarSolicitudes(Integer id, Integer idUsuario, Integer idAnimal, Integer idEstado) {
		return solicitudRepository.buscarSolicitudesPorFiltros(id, idUsuario, idAnimal, idEstado);
	}

	@Override
	public List<SolicitudDTO> listarSolicitudesPorUsuario(Integer idUsuario) {
		return solicitudRepository.listarPorUsuario(idUsuario);
	}

	@Override
	@Transactional
	public Integer cambiarEstado(Integer idSolicitud, Integer idEstadoNuevo, String comentarioAdmin) {

		SolicitudAdopcionEntity s = solicitudRepository.findById(idSolicitud).orElse(null);
		EstadoSolicitudEntity nuevoEstado = estadoRepository.findById(idEstadoNuevo).orElse(null);
		if (s == null || nuevoEstado == null)
			return 0;

		EstadoSolicitudEntity estadoAnterior = s.getEstado();

		s.setEstado(nuevoEstado);
		solicitudRepository.save(s);

		HistorialEstadoSolicitudEntity h = new HistorialEstadoSolicitudEntity(s, estadoAnterior, nuevoEstado,
				LocalDateTime.now(), comentarioAdmin);
		historialRepository.save(h);

		AnimalEntity animal = s.getAnimal();

		if ("APROBADA".equals(nuevoEstado.getNombre())) {

			animal.setEstado(AnimalEntity.EstadoAnimal.RESERVADO);
			animalRepository.save(animal);

		} else if ("FINALIZADA".equals(nuevoEstado.getNombre())) {

			animal.setEstado(AnimalEntity.EstadoAnimal.ADOPTADO);
			animalRepository.save(animal);

			EstadoSolicitudEntity estadoRechazada = estadoRepository.findByNombre("RECHAZADA");
			List<SolicitudDTO> otrasSolicitudes = solicitudRepository.buscarSolicitudesPorFiltros(null, null,
					animal.getIdAnimal(), null);

			for (SolicitudDTO otraDto : otrasSolicitudes) {
				if (!otraDto.getIdSolicitud().equals(s.getIdSolicitud())) {
					SolicitudAdopcionEntity otra = solicitudRepository.findById(otraDto.getIdSolicitud()).orElse(null);
					if (otra != null && !"RECHAZADA".equals(otra.getEstado().getNombre())
							&& !"FINALIZADA".equals(otra.getEstado().getNombre())) {
						EstadoSolicitudEntity estadoOriginal = otra.getEstado();
						otra.setEstado(estadoRechazada);
						solicitudRepository.save(otra);

						HistorialEstadoSolicitudEntity hAuto = new HistorialEstadoSolicitudEntity(otra, estadoOriginal,
								estadoRechazada, LocalDateTime.now(),
								"Cerrada automáticamente: el animal ha sido adoptado por otro solicitante.");
						historialRepository.save(hAuto);
					}
				}
			}

		} else if ("RECHAZADA".equals(nuevoEstado.getNombre())) {
			if ("APROBADA".equals(estadoAnterior.getNombre())) {
				long otrasAprobadas = solicitudRepository
						.buscarSolicitudesPorFiltros(null, null, animal.getIdAnimal(), 2).stream()
						.filter(o -> !o.getIdSolicitud().equals(s.getIdSolicitud())).count();
				if (otrasAprobadas == 0) {
					animal.setEstado(AnimalEntity.EstadoAnimal.DISPONIBLE);
					animalRepository.save(animal);
				}
			}
		}

		String asunto = "Tu solicitud de adopción ha cambiado de estado";
		String mensaje = "Hola " + s.getUsuario().getNombre() + ", tu solicitud para " + animal.getNombre()
				+ " pasó de " + (estadoAnterior == null ? "INICIO" : estadoAnterior.getNombre()) + " a "
				+ nuevoEstado.getNombre() + (comentarioAdmin != null ? ". Comentario: " + comentarioAdmin : ".");

		NotificacionEntity n = new NotificacionEntity(s.getUsuario(), s, asunto, mensaje);
		notificacionRepository.save(n);

		try {
			emailService.notificarCambioEstadoSolicitud(s.getUsuario().getEmail(), s.getUsuario().getNombre(),
					animal.getNombre(), estadoAnterior == null ? null : estadoAnterior.getNombre(),
					nuevoEstado.getNombre(), comentarioAdmin);
			n.setEnviado(true);
			n.setFechaEnvio(LocalDateTime.now());
			notificacionRepository.save(n);
		} catch (Exception ex) {
			System.err.println("No se pudo enviar el email: " + ex.getMessage());
		}

		return s.getIdSolicitud();
	}

	@Override
	@Transactional
	public Integer cancelarSolicitud(Integer idSolicitud, Integer idUsuario) {
		SolicitudAdopcionEntity s = solicitudRepository.findById(idSolicitud).orElse(null);
		if (s == null)
			return 0;

		if (!s.getUsuario().getIdUsuario().equals(idUsuario))
			return -1;

		if (!"EN_REVISION".equals(s.getEstado().getNombre()))
			return -2;

		solicitudRepository.deleteById(idSolicitud);
		return idSolicitud;
	}

	@Override
	public List<HistorialEstadoDTO> obtenerHistorial(Integer idSolicitud) {
		return historialRepository.listarHistorialPorSolicitud(idSolicitud);
	}
}