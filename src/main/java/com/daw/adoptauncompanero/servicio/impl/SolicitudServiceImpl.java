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

// =============================================================
// SERVICIO SOLICITUDES (2.3.4 - Proceso de adopción)
// Implementa toda la lógica de negocio (2.2.5):
//  - inicio de solicitud
//  - cambio de estado (genera historial + email automático)
//  - cancelación por parte del cliente
// =============================================================
@Service
public class SolicitudServiceImpl implements SolicitudService {

    @Autowired private SolicitudAdopcionRepository solicitudRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private AnimalRepository animalRepository;
    @Autowired private EstadoSolicitudRepository estadoRepository;
    @Autowired private HistorialEstadoRepository historialRepository;
    @Autowired private NotificacionRepository notificacionRepository;
    @Autowired private EmailService emailService;

    // -----------------------------------------------------------
    // 2.2.3.4 - Iniciar solicitud
    // -----------------------------------------------------------
    @Override
    @Transactional
    public Integer iniciarSolicitud(Integer idUsuario, Integer idAnimal, String comentarios) {

        UsuarioEntity usuario = usuarioRepository.findById(idUsuario).orElse(null);
        AnimalEntity animal = animalRepository.findById(idAnimal).orElse(null);
        if (usuario == null || animal == null) return 0;

        // Validación: el animal NO puede haber sido ya adoptado.
        // Permitimos solicitudes a animales DISPONIBLES y RESERVADOS,
        // así varios usuarios pueden competir por el mismo animal.
        if (animal.getEstado() == AnimalEntity.EstadoAnimal.ADOPTADO) return -1;

        // Validación: este usuario no ha solicitado YA este animal
        // (mantenemos la unique constraint del SQL: 1 solicitud por usuario+animal)
        if (solicitudRepository.contarSolicitudesUsuarioAnimal(idUsuario, idAnimal) > 0) return -2;

        // Estado inicial: EN_REVISION
        EstadoSolicitudEntity estadoInicial = estadoRepository.findByNombre("EN_REVISION");

        SolicitudAdopcionEntity s = new SolicitudAdopcionEntity();
        s.setUsuario(usuario);
        s.setAnimal(animal);
        s.setEstado(estadoInicial);
        s.setComentarios(comentarios);
        s.setFechaSolicitud(LocalDateTime.now());

        Integer idGenerado = solicitudRepository.save(s).getIdSolicitud();

        // Registrar entrada inicial del historial (2.2.5)
        HistorialEstadoSolicitudEntity h = new HistorialEstadoSolicitudEntity(
                s, null, estadoInicial, LocalDateTime.now(), "Solicitud creada");
        historialRepository.save(h);

        return idGenerado;
    }
    // -----------------------------------------------------------
    // 2.2.4.2 / 2.4.6 - Listado solicitudes (admin)
    // -----------------------------------------------------------
    @Override
    public List<SolicitudDTO> buscarSolicitudes(Integer id, Integer idUsuario, Integer idAnimal, Integer idEstado) {
        return solicitudRepository.buscarSolicitudesPorFiltros(id, idUsuario, idAnimal, idEstado);
    }

    // -----------------------------------------------------------
    // 2.2.3.5 - Cliente consulta sus solicitudes
    // -----------------------------------------------------------
    @Override
    public List<SolicitudDTO> listarSolicitudesPorUsuario(Integer idUsuario) {
        return solicitudRepository.listarPorUsuario(idUsuario);
    }

    // -----------------------------------------------------------
    // 2.2.4.3 - Cambiar estado (lógica de negocio 2.2.5 / 2.4.7)
    // -----------------------------------------------------------
    @Override
    @Transactional
    public Integer cambiarEstado(Integer idSolicitud, Integer idEstadoNuevo, String comentarioAdmin) {

        SolicitudAdopcionEntity s = solicitudRepository.findById(idSolicitud).orElse(null);
        EstadoSolicitudEntity nuevoEstado = estadoRepository.findById(idEstadoNuevo).orElse(null);
        if (s == null || nuevoEstado == null) return 0;

        EstadoSolicitudEntity estadoAnterior = s.getEstado();

        // 1. Actualizar estado de la solicitud
        s.setEstado(nuevoEstado);
        solicitudRepository.save(s);

        // 2. Registrar historial (2.2.5)
        HistorialEstadoSolicitudEntity h = new HistorialEstadoSolicitudEntity(
                s, estadoAnterior, nuevoEstado, LocalDateTime.now(), comentarioAdmin);
        historialRepository.save(h);

        // 3. Reglas de negocio según nuevo estado
        AnimalEntity animal = s.getAnimal();

        if ("APROBADA".equals(nuevoEstado.getNombre())) {
            // El animal pasa a RESERVADO (queda apartado para este adoptante)
            animal.setEstado(AnimalEntity.EstadoAnimal.RESERVADO);
            animalRepository.save(animal);

        } else if ("FINALIZADA".equals(nuevoEstado.getNombre())) {
            // 2.2.4.5 - Animal ADOPTADO definitivamente.
            // Las demás solicitudes pendientes para este animal se marcan
            // automáticamente como RECHAZADAS (porque ya hay un adoptante).
            animal.setEstado(AnimalEntity.EstadoAnimal.ADOPTADO);
            animalRepository.save(animal);

            EstadoSolicitudEntity estadoRechazada = estadoRepository.findByNombre("RECHAZADA");
            List<SolicitudDTO> otrasSolicitudes = solicitudRepository
                    .buscarSolicitudesPorFiltros(null, null, animal.getIdAnimal(), null);

            for (SolicitudDTO otraDto : otrasSolicitudes) {
                if (!otraDto.getIdSolicitud().equals(s.getIdSolicitud())) {
                    SolicitudAdopcionEntity otra = solicitudRepository
                            .findById(otraDto.getIdSolicitud()).orElse(null);
                    if (otra != null
                        && !"RECHAZADA".equals(otra.getEstado().getNombre())
                        && !"FINALIZADA".equals(otra.getEstado().getNombre())) {
                        EstadoSolicitudEntity estadoOriginal = otra.getEstado();
                        otra.setEstado(estadoRechazada);
                        solicitudRepository.save(otra);

                        // Historial de cierre automático
                        HistorialEstadoSolicitudEntity hAuto = new HistorialEstadoSolicitudEntity(
                                otra, estadoOriginal, estadoRechazada, LocalDateTime.now(),
                                "Cerrada automáticamente: el animal ha sido adoptado por otro solicitante.");
                        historialRepository.save(hAuto);
                    }
                }
            }

        } else if ("RECHAZADA".equals(nuevoEstado.getNombre())) {
            // Si esta solicitud era la APROBADA y se rechaza ahora,
            // el animal vuelve a DISPONIBLE solo si NO hay otra solicitud aprobada.
            if ("APROBADA".equals(estadoAnterior.getNombre())) {
                long otrasAprobadas = solicitudRepository
                        .buscarSolicitudesPorFiltros(null, null, animal.getIdAnimal(), 2)  // 2 = APROBADA
                        .stream()
                        .filter(o -> !o.getIdSolicitud().equals(s.getIdSolicitud()))
                        .count();
                if (otrasAprobadas == 0) {
                    animal.setEstado(AnimalEntity.EstadoAnimal.DISPONIBLE);
                    animalRepository.save(animal);
                }
            }
        }

        // 4. Email automático
        String asunto = "Tu solicitud de adopción ha cambiado de estado";
        String mensaje = "Hola " + s.getUsuario().getNombre()
                + ", tu solicitud para " + animal.getNombre()
                + " pasó de " + (estadoAnterior == null ? "INICIO" : estadoAnterior.getNombre())
                + " a " + nuevoEstado.getNombre()
                + (comentarioAdmin != null ? ". Comentario: " + comentarioAdmin : ".");

        NotificacionEntity n = new NotificacionEntity(s.getUsuario(), s, asunto, mensaje);
        notificacionRepository.save(n);

        try {
            emailService.notificarCambioEstadoSolicitud(
                    s.getUsuario().getEmail(),
                    s.getUsuario().getNombre(),
                    animal.getNombre(),
                    estadoAnterior == null ? null : estadoAnterior.getNombre(),
                    nuevoEstado.getNombre(),
                    comentarioAdmin);
            n.setEnviado(true);
            n.setFechaEnvio(LocalDateTime.now());
            notificacionRepository.save(n);
        } catch (Exception ex) {
            System.err.println("No se pudo enviar el email: " + ex.getMessage());
        }

        return s.getIdSolicitud();
    }

    // -----------------------------------------------------------
    // 2.2.3.7 - Cancelar solicitud (solo si está EN_REVISION)
    // -----------------------------------------------------------
    @Override
    @Transactional
    public Integer cancelarSolicitud(Integer idSolicitud, Integer idUsuario) {
        SolicitudAdopcionEntity s = solicitudRepository.findById(idSolicitud).orElse(null);
        if (s == null) return 0;

        // Validación: solo el dueño puede cancelar
        if (!s.getUsuario().getIdUsuario().equals(idUsuario)) return -1;

        // Validación: solo en estado inicial EN_REVISION
        if (!"EN_REVISION".equals(s.getEstado().getNombre())) return -2;

        solicitudRepository.deleteById(idSolicitud);
        return idSolicitud;
    }

    // -----------------------------------------------------------
    // 2.4.9 - Historial completo (lo ven cliente y admin)
    // -----------------------------------------------------------
    @Override
    public List<HistorialEstadoDTO> obtenerHistorial(Integer idSolicitud) {
        return historialRepository.listarHistorialPorSolicitud(idSolicitud);
    }
}