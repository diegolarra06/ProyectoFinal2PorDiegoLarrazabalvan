package com.daw.adoptauncompanero.servicio.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.adoptauncompanero.dtos.CitaDTO;
import com.daw.adoptauncompanero.entities.CitaAdopcionEntity;
import com.daw.adoptauncompanero.entities.SolicitudAdopcionEntity;
import com.daw.adoptauncompanero.repositorios.CitaAdopcionRepository;
import com.daw.adoptauncompanero.repositorios.SolicitudAdopcionRepository;
import com.daw.adoptauncompanero.servicio.interfaces.CitaService;
import com.daw.adoptauncompanero.servicio.interfaces.EmailService;


@Service
public class CitaServiceImpl implements CitaService {

    @Autowired private CitaAdopcionRepository citaRepository;
    @Autowired private SolicitudAdopcionRepository solicitudRepository;
    @Autowired private EmailService emailService;

    @Override
    @Transactional
    public Integer programarCita(Integer idSolicitud, LocalDateTime fechaCita, String observaciones) {
        SolicitudAdopcionEntity s = solicitudRepository.findById(idSolicitud).orElse(null);
        if (s == null) return 0;

        CitaAdopcionEntity c = new CitaAdopcionEntity();
        c.setFechaCita(fechaCita);
        c.setObservaciones(observaciones);
        c.setSolicitud(s);
        Integer id = citaRepository.save(c).getIdCita();

        try {
            emailService.notificarCitaProgramada(
                    s.getUsuario().getEmail(),
                    s.getUsuario().getNombre(),
                    s.getAnimal().getNombre(),
                    fechaCita.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    observaciones);
        } catch (Exception ex) {
            System.err.println("Error enviando email de cita: " + ex.getMessage());
        }

        return id;
    }

    @Override
    @Transactional
    public Integer actualizarCita(Integer idCita, LocalDateTime fechaCita, String observaciones) {
        CitaAdopcionEntity c = citaRepository.findById(idCita).orElse(null);
        if (c == null) return 0;
        c.setFechaCita(fechaCita);
        c.setObservaciones(observaciones);
        return citaRepository.save(c).getIdCita();
    }

    @Override
    public Integer borrarCita(Integer idCita) {
        if (!citaRepository.existsById(idCita)) return 0;
        citaRepository.deleteById(idCita);
        return idCita;
    }

    @Override
    public List<CitaDTO> listarTodasLasCitas() {
        return citaRepository.listarTodasLasCitas();
    }

    @Override
    public List<CitaDTO> listarCitasPorUsuario(Integer idUsuario) {
        return citaRepository.listarCitasPorUsuario(idUsuario);
    }
}
