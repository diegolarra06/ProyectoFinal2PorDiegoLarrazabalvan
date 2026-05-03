package com.daw.adoptauncompanero.controladores;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.adoptauncompanero.entities.UsuarioEntity;
import com.daw.adoptauncompanero.servicio.interfaces.SolicitudService;
import com.daw.adoptauncompanero.servicio.interfaces.UsuarioService;

// =============================================================
// CONTROLADOR DE SOLICITUDES (2.3.4 Proceso de adopción)
// =============================================================
@Controller
@RequestMapping("/solicitudes")
public class SolicitudesController {

    @Autowired private SolicitudService solicitudService;
    @Autowired private UsuarioService usuarioService;

    // -------------------------
    // 2.2.3.4 - Cliente inicia solicitud
    // -------------------------
    @GetMapping("/iniciar/{idAnimal}")
    public String iniciarGet(@PathVariable Integer idAnimal, Model model) {
        model.addAttribute("idAnimal", idAnimal);
        model.addAttribute("resultado", null);
        return "solicitudes/iniciarSolicitud";
    }

    @PostMapping("/iniciar")
    public String iniciarPost(@RequestParam Integer idAnimal,
                              @RequestParam(required = false) String comentarios,
                              Principal principal,
                              Model model) {

        // Identificamos al cliente logueado por su email
        UsuarioEntity usuario = usuarioService.obtenerPorEmail(principal.getName());
        Integer res = solicitudService.iniciarSolicitud(usuario.getIdUsuario(), idAnimal, comentarios);

        model.addAttribute("idAnimal", idAnimal);
        model.addAttribute("resultado", res);
        return "solicitudes/iniciarSolicitud";
    }

    // -------------------------
    // 2.2.3.5 - Cliente consulta sus solicitudes
    // -------------------------
    @GetMapping("/misSolicitudes")
    public String misSolicitudes(Principal principal, Model model) {
        UsuarioEntity usuario = usuarioService.obtenerPorEmail(principal.getName());
        model.addAttribute("lista", solicitudService.listarSolicitudesPorUsuario(usuario.getIdUsuario()));
        return "solicitudes/misSolicitudes";
    }

    // -------------------------
    // 2.4.9 - Historial de una solicitud (cliente o admin)
    // -------------------------
    @GetMapping("/historial/{idSolicitud}")
    public String historial(@PathVariable Integer idSolicitud, Model model) {
        model.addAttribute("historial", solicitudService.obtenerHistorial(idSolicitud));
        model.addAttribute("idSolicitud", idSolicitud);
        return "solicitudes/historial";
    }

    // -------------------------
    // 2.2.3.7 - Cliente cancela su solicitud (solo si está EN_REVISION)
    // -------------------------
    @PostMapping("/cancelar")
    public String cancelar(@RequestParam Integer idSolicitud, Principal principal, Model model) {
        UsuarioEntity usuario = usuarioService.obtenerPorEmail(principal.getName());
        Integer res = solicitudService.cancelarSolicitud(idSolicitud, usuario.getIdUsuario());
        model.addAttribute("resultado", res);
        model.addAttribute("lista", solicitudService.listarSolicitudesPorUsuario(usuario.getIdUsuario()));
        return "solicitudes/misSolicitudes";
    }

    // -------------------------
    // 2.2.4.2 - LISTADO ADMIN de todas las solicitudes
    // -------------------------
    @GetMapping("/listadoAdmin")
    public String listadoAdminGet(Model model) {
        model.addAttribute("lista", null);
        return "solicitudes/listadoAdmin";
    }

    @PostMapping("/listadoAdmin")
    public String listadoAdminPost(@RequestParam(required = false) Integer id,
                                   @RequestParam(required = false) Integer idUsuario,
                                   @RequestParam(required = false) Integer idAnimal,
                                   @RequestParam(required = false) Integer idEstado,
                                   Model model) {
        model.addAttribute("lista",
                solicitudService.buscarSolicitudes(id, idUsuario, idAnimal, idEstado));
        return "solicitudes/listadoAdmin";
    }

    // -------------------------
    // 2.2.4.3 - Admin cambia estado de una solicitud
    // -------------------------
    @PostMapping("/cambiarEstado")
    public String cambiarEstado(@RequestParam Integer idSolicitud,
                                @RequestParam Integer idEstadoNuevo,
                                @RequestParam(required = false) String comentarioAdmin,
                                Model model) {

        Integer res = solicitudService.cambiarEstado(idSolicitud, idEstadoNuevo, comentarioAdmin);
        model.addAttribute("resultado", res);
        model.addAttribute("lista", solicitudService.buscarSolicitudes(null, null, null, null));
        return "solicitudes/listadoAdmin";
    }
}