package com.daw.adoptauncompanero.controladores.rest;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.daw.adoptauncompanero.dtos.AnimalDTO;
import com.daw.adoptauncompanero.dtos.CitaDTO;
import com.daw.adoptauncompanero.dtos.EstadisticasDTO;
import com.daw.adoptauncompanero.dtos.FavoritoDTO;
import com.daw.adoptauncompanero.dtos.HistorialEstadoDTO;
import com.daw.adoptauncompanero.dtos.NoticiaDTO;
import com.daw.adoptauncompanero.dtos.SolicitudDTO;
import com.daw.adoptauncompanero.dtos.UsuarioDTO;

import com.daw.adoptauncompanero.entities.AnimalEntity;
import com.daw.adoptauncompanero.entities.ImagenAnimalEntity;
import com.daw.adoptauncompanero.entities.NoticiaEntity;
import com.daw.adoptauncompanero.entities.UsuarioEntity;

import com.daw.adoptauncompanero.repositorios.ImagenAnimalRepository;

import com.daw.adoptauncompanero.servicio.interfaces.AnimalService;
import com.daw.adoptauncompanero.servicio.interfaces.CitaService;
import com.daw.adoptauncompanero.servicio.interfaces.EstadisticasService;
import com.daw.adoptauncompanero.servicio.interfaces.FavoritoService;
import com.daw.adoptauncompanero.servicio.interfaces.NoticiaService;
import com.daw.adoptauncompanero.servicio.interfaces.SolicitudService;
import com.daw.adoptauncompanero.servicio.interfaces.UsuarioService;

// =============================================================
// CONTROLADOR REST PARA EL FRONTEND VUE
// Devuelve TODO en JSON para que el SPA Vue pueda consumirlo.
// No reemplaza los @Controller de Thymeleaf, los complementa.
// Todas las rutas empiezan por /api/
// =============================================================
@RestController
@RequestMapping("/api")
public class ApiRestController {

    @Autowired
    private AnimalService animalService;

    @Autowired
    private SolicitudService solicitudService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private FavoritoService favoritoService;

    @Autowired
    private CitaService citaService;

    @Autowired
    private EstadisticasService estadisticasService;

    @Autowired
    private ImagenAnimalRepository imagenRepository;

    @Autowired
    private NoticiaService noticiaService;

    // =============================================================
    // MÉTODOS AUXILIARES
    // =============================================================

    private Integer toInteger(Object valor) {
        if (valor == null) return null;
        if (valor instanceof Integer) return (Integer) valor;
        if (valor instanceof Number) return ((Number) valor).intValue();

        String texto = valor.toString().trim();
        if (texto.isEmpty()) return null;

        return Integer.parseInt(texto);
    }

    private Boolean toBoolean(Object valor, Boolean valorPorDefecto) {
        if (valor == null) return valorPorDefecto;
        if (valor instanceof Boolean) return (Boolean) valor;

        String texto = valor.toString().trim();
        if (texto.isEmpty()) return valorPorDefecto;

        return Boolean.parseBoolean(texto);
    }

    private String toStringOrNull(Object valor) {
        if (valor == null) return null;
        String texto = valor.toString().trim();
        return texto.isEmpty() ? null : texto;
    }

    // =============================================================
    // ANIMALES
    // =============================================================

    // Listado paginado de animales.
    // Frontend: GET /api/animales?page=0&size=8&sort=idAnimal
    @GetMapping("/animales")
    public ResponseEntity<Page<AnimalDTO>> listarAnimalesPaginados(
            @PageableDefault(size = 8, sort = "idAnimal") Pageable pageable) {

        Page<AnimalDTO> pagina = animalService.listarAnimalesPaginados(pageable);
        return ResponseEntity.ok(pagina);
    }

    // Devuelve las especies que existen en la BBDD para los filtros del catálogo.
    // Frontend: GET /api/animales/especies
    @GetMapping("/animales/especies")
    public ResponseEntity<List<String>> listarEspecies() {
        return ResponseEntity.ok(animalService.listarEspeciesDistintas());
    }

    // Búsqueda con filtros.
    // Frontend: GET /api/animales/buscar?nombre=&especie=&tamano=&estado=
    @GetMapping("/animales/buscar")
    public ResponseEntity<List<AnimalDTO>> buscarAnimales(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String especie,
            @RequestParam(required = false) Integer edadMin,
            @RequestParam(required = false) Integer edadMax,
            @RequestParam(required = false) String tamano,
            @RequestParam(required = false) String estado) {

        List<AnimalDTO> animales = animalService.buscarAnimales(
                null,
                nombre,
                especie,
                edadMin,
                edadMax,
                tamano,
                estado
        );

        return ResponseEntity.ok(animales);
    }

    // Ficha de un animal individual.
    // Frontend: GET /api/animales/1
    @GetMapping("/animales/{id}")
    public ResponseEntity<Map<String, Object>> obtenerAnimal(@PathVariable Integer id) {
        AnimalEntity a = animalService.obtenerAnimalPorId(id);

        if (a == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("idAnimal", a.getIdAnimal());
        respuesta.put("id", a.getIdAnimal());
        respuesta.put("nombre", a.getNombre());
        respuesta.put("especie", a.getEspecie());
        respuesta.put("edad", a.getEdad());
        respuesta.put("tamano", a.getTamano());
        respuesta.put("personalidad", a.getPersonalidad());
        respuesta.put("necesidadesEspeciales", a.getNecesidadesEspeciales());
        respuesta.put("estadoSanitario", a.getEstadoSanitario());
        respuesta.put("estado", a.getEstado() != null ? a.getEstado().name() : null);

        List<Map<String, Object>> imagenes = a.getImagenes() == null
                ? List.of()
                : a.getImagenes().stream().map(i -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("idImagen", i.getIdImagen());
                    m.put("urlImagen", i.getUrlImagen());
                    m.put("urlCompleta", "http://localhost:8080/uploads/" + i.getUrlImagen());
                    return m;
                }).toList();

        respuesta.put("imagenes", imagenes);

        return ResponseEntity.ok(respuesta);
    }

    // Crear animal desde admin.
    // Frontend: POST /api/animales
    @PostMapping("/animales")
    public ResponseEntity<Integer> crearAnimal(@RequestBody Map<String, Object> body) {
        Integer id = animalService.insertarAnimal(
                toStringOrNull(body.get("nombre")),
                toStringOrNull(body.get("especie")),
                toInteger(body.get("edad")),
                toStringOrNull(body.get("tamano")),
                toStringOrNull(body.get("personalidad")),
                toStringOrNull(body.get("necesidadesEspeciales")),
                toStringOrNull(body.get("estadoSanitario")),
                toStringOrNull(body.getOrDefault("estado", "DISPONIBLE"))
        );

        return ResponseEntity.ok(id);
    }

    // Actualizar animal desde admin.
    // Frontend: PUT /api/animales/1
    @PutMapping("/animales/{id}")
    public ResponseEntity<Integer> actualizarAnimal(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {

        Integer res = animalService.actualizarAnimal(
                id,
                toStringOrNull(body.get("nombre")),
                toStringOrNull(body.get("especie")),
                toInteger(body.get("edad")),
                toStringOrNull(body.get("tamano")),
                toStringOrNull(body.get("personalidad")),
                toStringOrNull(body.get("necesidadesEspeciales")),
                toStringOrNull(body.get("estadoSanitario")),
                toStringOrNull(body.get("estado"))
        );

        return ResponseEntity.ok(res);
    }

    // Borrar animal desde admin.
    // Frontend: DELETE /api/animales/1
    @DeleteMapping("/animales/{id}")
    public ResponseEntity<Integer> borrarAnimal(@PathVariable Integer id) {
        return ResponseEntity.ok(animalService.borrarAnimal(id));
    }

    // =============================================================
    // AUTENTICACIÓN / SESIÓN
    // =============================================================

    // Devuelve los datos del usuario logueado.
    // Frontend: GET /api/me
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> miSesion(Principal principal) {
        if (principal == null) {
            return ResponseEntity.ok(Map.of("autenticado", false));
        }

        UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());

        if (u == null) {
            return ResponseEntity.ok(Map.of("autenticado", false));
        }

        String rol = u.getUsuarioRoles() == null
                ? "CLIENTE"
                : u.getUsuarioRoles().stream()
                    .findFirst()
                    .map(ur -> ur.getRol().getNombre())
                    .orElse("CLIENTE");

        Map<String, Object> resp = new HashMap<>();
        resp.put("autenticado", true);
        resp.put("idUsuario", u.getIdUsuario());
        resp.put("nombre", u.getNombre());
        resp.put("email", u.getEmail());
        resp.put("telefono", u.getTelefono());
        resp.put("direccion", u.getDireccion());
        resp.put("rol", rol);

        return ResponseEntity.ok(resp);
    }

    // =============================================================
    // REGISTRO PÚBLICO
    // =============================================================

    // Registro de cliente.
    // Frontend: POST /api/registro
    @PostMapping("/registro")
    public ResponseEntity<Map<String, Object>> registrarCliente(@RequestBody Map<String, Object> body) {
        Integer res = usuarioService.registrarCliente(
                toStringOrNull(body.get("nombre")),
                toStringOrNull(body.get("email")),
                toStringOrNull(body.get("password")),
                toStringOrNull(body.get("telefono")),
                toStringOrNull(body.get("direccion"))
        );

        Map<String, Object> resp = new HashMap<>();

        if (res != null && res > 0) {
            resp.put("ok", true);
            resp.put("idUsuario", res);
        } else {
            resp.put("ok", false);
            resp.put("error", res != null && res == -1 ? "Email ya registrado" : "Error desconocido");
        }

        return ResponseEntity.ok(resp);
    }

    // =============================================================
    // SOLICITUDES
    // =============================================================

    // Solicitudes del usuario logueado.
    // Frontend: GET /api/solicitudes/mias
    @GetMapping("/solicitudes/mias")
    public ResponseEntity<List<SolicitudDTO>> misSolicitudes(Principal principal) {
        UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());
        return ResponseEntity.ok(solicitudService.listarSolicitudesPorUsuario(u.getIdUsuario()));
    }

    // Todas las solicitudes para admin.
    // Frontend: GET /api/solicitudes
    @GetMapping("/solicitudes")
    public ResponseEntity<List<SolicitudDTO>> todasSolicitudes() {
        return ResponseEntity.ok(solicitudService.buscarSolicitudes(null, null, null, null));
    }

    // Historial de una solicitud.
    // Frontend: GET /api/solicitudes/1/historial
    @GetMapping("/solicitudes/{id}/historial")
    public ResponseEntity<List<HistorialEstadoDTO>> historialSolicitud(@PathVariable Integer id) {
        return ResponseEntity.ok(solicitudService.obtenerHistorial(id));
    }

    // Iniciar solicitud de adopción.
    // Frontend: POST /api/solicitudes
    @PostMapping("/solicitudes")
    public ResponseEntity<Integer> iniciarSolicitud(
            @RequestBody Map<String, Object> body,
            Principal principal) {

        UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());

        Integer idAnimal = toInteger(body.get("idAnimal"));
        String comentarios = toStringOrNull(body.get("comentarios"));
        if (comentarios == null) comentarios = "";

        Integer res = solicitudService.iniciarSolicitud(u.getIdUsuario(), idAnimal, comentarios);

        return ResponseEntity.ok(res);
    }

    // Cambiar estado de solicitud desde admin.
    // Frontend: PUT /api/solicitudes/1/estado
    @PutMapping("/solicitudes/{id}/estado")
    public ResponseEntity<Integer> cambiarEstadoSolicitud(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {

        Integer idEstadoNuevo = toInteger(body.get("idEstadoNuevo"));
        String comentarioAdmin = toStringOrNull(body.get("comentarioAdmin"));

        Integer res = solicitudService.cambiarEstado(id, idEstadoNuevo, comentarioAdmin);

        return ResponseEntity.ok(res);
    }

    // Cancelar solicitud.
    // Frontend: DELETE /api/solicitudes/1
    @DeleteMapping("/solicitudes/{id}")
    public ResponseEntity<Integer> cancelarSolicitud(
            @PathVariable Integer id,
            Principal principal) {

        UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());
        Integer res = solicitudService.cancelarSolicitud(id, u.getIdUsuario());

        return ResponseEntity.ok(res);
    }

    // =============================================================
    // FAVORITOS
    // =============================================================

    // Mis favoritos.
    // Frontend: GET /api/favoritos
    @GetMapping("/favoritos")
    public ResponseEntity<List<FavoritoDTO>> misFavoritos(Principal principal) {
        UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());
        return ResponseEntity.ok(favoritoService.listarFavoritosPorUsuario(u.getIdUsuario()));
    }

    // Agregar favorito.
    // Frontend: POST /api/favoritos/1
    @PostMapping("/favoritos/{idAnimal}")
    public ResponseEntity<Integer> agregarFavorito(
            @PathVariable Integer idAnimal,
            Principal principal) {

        UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());
        return ResponseEntity.ok(favoritoService.agregarFavorito(u.getIdUsuario(), idAnimal));
    }

    // Quitar favorito.
    // Frontend: DELETE /api/favoritos/1
    @DeleteMapping("/favoritos/{idAnimal}")
    public ResponseEntity<Integer> quitarFavorito(
            @PathVariable Integer idAnimal,
            Principal principal) {

        UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());
        return ResponseEntity.ok(favoritoService.quitarFavorito(u.getIdUsuario(), idAnimal));
    }

    // =============================================================
    // USUARIOS
    // =============================================================

    // Listar usuarios para admin.
    // Frontend: GET /api/usuarios
    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.buscarUsuarios(null, null, null));
    }

    // Actualizar datos personales.
    // Frontend: PUT /api/usuarios/1
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Integer> actualizarMisDatos(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {

        Integer res = usuarioService.actualizarDatosPersonales(
                id,
                toStringOrNull(body.get("nombre")),
                toStringOrNull(body.get("telefono")),
                toStringOrNull(body.get("direccion"))
        );

        return ResponseEntity.ok(res);
    }

    // Borrar usuario.
    // Frontend: DELETE /api/usuarios/1
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Integer> borrarUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.borrarUsuario(id));
    }

    // =============================================================
    // CITAS
    // =============================================================

    // Listar citas.
    // Frontend: GET /api/citas
    @GetMapping("/citas")
    public ResponseEntity<List<CitaDTO>> listarCitas() {
        return ResponseEntity.ok(citaService.listarTodasLasCitas());
    }

    // Programar cita.
    // Frontend: POST /api/citas
    @PostMapping("/citas")
    public ResponseEntity<Integer> programarCita(@RequestBody Map<String, Object> body) {
        Integer idSolicitud = toInteger(body.get("idSolicitud"));
        LocalDateTime fechaCita = LocalDateTime.parse(toStringOrNull(body.get("fechaCita")));
        String observaciones = toStringOrNull(body.get("observaciones"));

        Integer res = citaService.programarCita(idSolicitud, fechaCita, observaciones);

        return ResponseEntity.ok(res);
    }

    // Borrar cita.
    // Frontend: DELETE /api/citas/1
    @DeleteMapping("/citas/{id}")
    public ResponseEntity<Integer> borrarCita(@PathVariable Integer id) {
        return ResponseEntity.ok(citaService.borrarCita(id));
    }

    // =============================================================
    // ESTADÍSTICAS
    // =============================================================

    // Estadísticas del panel admin.
    // Frontend: GET /api/estadisticas
    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasDTO> estadisticas() {
        return ResponseEntity.ok(estadisticasService.calcularEstadisticas());
    }

    // =============================================================
    // IMÁGENES DE ANIMALES
    // =============================================================

    // Listar imágenes de un animal.
    // Frontend: GET /api/animales/1/imagenes
    @GetMapping("/animales/{idAnimal}/imagenes")
    public ResponseEntity<List<Map<String, Object>>> listarImagenesAnimal(@PathVariable Integer idAnimal) {
        List<ImagenAnimalEntity> imgs = imagenRepository.listarPorAnimal(idAnimal);

        List<Map<String, Object>> respuesta = imgs.stream().map(i -> {
            Map<String, Object> m = new HashMap<>();
            m.put("idImagen", i.getIdImagen());
            m.put("urlImagen", i.getUrlImagen());
            m.put("urlCompleta", "http://localhost:8080/uploads/" + i.getUrlImagen());
            return m;
        }).toList();

        return ResponseEntity.ok(respuesta);
    }

    // Subir imagen para un animal.
    // Frontend: POST /api/animales/1/imagenes
    @PostMapping("/animales/{idAnimal}/imagenes")
    public ResponseEntity<Map<String, Object>> subirImagenAnimal(
            @PathVariable Integer idAnimal,
            @RequestParam("archivo") MultipartFile archivo) {

        Map<String, Object> resp = new HashMap<>();

        try {
            Integer idImagen = animalService.subirImagen(idAnimal, archivo);

            if (idImagen == null || idImagen == 0) {
                resp.put("ok", false);
                resp.put("error", "No se encontró el animal o el archivo no es válido");
                return ResponseEntity.badRequest().body(resp);
            }

            resp.put("ok", true);
            resp.put("idImagen", idImagen);
            return ResponseEntity.ok(resp);

        } catch (IllegalArgumentException ex) {
            resp.put("ok", false);
            resp.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(resp);

        } catch (Exception ex) {
            resp.put("ok", false);
            resp.put("error", "Error guardando el archivo: " + ex.getMessage());
            return ResponseEntity.internalServerError().body(resp);
        }
    }

    // Borrar imagen.
    // Frontend: DELETE /api/animales/imagenes/1
    @DeleteMapping("/animales/imagenes/{idImagen}")
    public ResponseEntity<Map<String, Object>> borrarImagenAnimal(@PathVariable Integer idImagen) {
        Map<String, Object> resp = new HashMap<>();

        Integer res = animalService.borrarImagen(idImagen);

        if (res == 0) {
            resp.put("ok", false);
            resp.put("error", "Imagen no encontrada");
            return ResponseEntity.status(404).body(resp);
        }

        resp.put("ok", true);
        return ResponseEntity.ok(resp);
    }

    // =============================================================
    // NOTICIAS
    // =============================================================

    // Listado público de noticias publicadas.
    // Frontend: GET /api/noticias
    @GetMapping("/noticias")
    public ResponseEntity<List<NoticiaDTO>> listarNoticias() {
        return ResponseEntity.ok(noticiaService.listarPublicadas());
    }

    // Noticias destacadas para el home.
    // Frontend: GET /api/noticias/destacadas?cantidad=5
    @GetMapping("/noticias/destacadas")
    public ResponseEntity<List<NoticiaDTO>> noticiasDestacadas(
            @RequestParam(defaultValue = "5") int cantidad) {

        return ResponseEntity.ok(noticiaService.listarDestacadas(cantidad));
    }

    // Todas las noticias para admin.
    // Frontend: GET /api/noticias/admin
    @GetMapping("/noticias/admin")
    public ResponseEntity<List<NoticiaDTO>> listarTodasNoticiasAdmin() {
        return ResponseEntity.ok(noticiaService.listarTodas());
    }

    // Ficha individual de una noticia.
    // Frontend: GET /api/noticias/1
    @GetMapping("/noticias/{id}")
    public ResponseEntity<Map<String, Object>> obtenerNoticia(@PathVariable Integer id) {
        NoticiaEntity n = noticiaService.obtenerPorId(id);

        if (n == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("idNoticia", n.getIdNoticia());
        resp.put("titulo", n.getTitulo());
        resp.put("subtitulo", n.getSubtitulo());
        resp.put("contenido", n.getContenido());
        resp.put("imagen", n.getImagen());
        resp.put("autor", n.getAutor());
        resp.put("fechaPublicacion", n.getFechaPublicacion());
        resp.put("publicada", n.getPublicada());

        return ResponseEntity.ok(resp);
    }

    // Crear noticia.
    // Frontend: POST /api/noticias
    @PostMapping("/noticias")
    public ResponseEntity<Integer> crearNoticia(@RequestBody Map<String, Object> body) {
        Integer id = noticiaService.crearNoticia(
                toStringOrNull(body.get("titulo")),
                toStringOrNull(body.get("subtitulo")),
                toStringOrNull(body.get("contenido")),
                toStringOrNull(body.get("autor")),
                toBoolean(body.get("publicada"), true)
        );

        return ResponseEntity.ok(id);
    }

    // Actualizar noticia.
    // Frontend: PUT /api/noticias/1
    @PutMapping("/noticias/{id}")
    public ResponseEntity<Integer> actualizarNoticia(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {

        Integer res = noticiaService.actualizarNoticia(
                id,
                toStringOrNull(body.get("titulo")),
                toStringOrNull(body.get("subtitulo")),
                toStringOrNull(body.get("contenido")),
                toStringOrNull(body.get("autor")),
                toBoolean(body.get("publicada"), true)
        );

        return ResponseEntity.ok(res);
    }

    // Borrar noticia.
    // Frontend: DELETE /api/noticias/1
    @DeleteMapping("/noticias/{id}")
    public ResponseEntity<Integer> borrarNoticia(@PathVariable Integer id) {
        return ResponseEntity.ok(noticiaService.borrarNoticia(id));
    }

    // Subir imagen de noticia.
    // Frontend: POST /api/noticias/1/imagen
    @PostMapping("/noticias/{id}/imagen")
    public ResponseEntity<Map<String, Object>> subirImagenNoticia(
            @PathVariable Integer id,
            @RequestParam("archivo") MultipartFile archivo) {

        Map<String, Object> resp = new HashMap<>();

        try {
            Integer res = noticiaService.subirImagenNoticia(id, archivo);

            if (res == 0) {
                resp.put("ok", false);
                resp.put("error", "Noticia no encontrada");
                return ResponseEntity.badRequest().body(resp);
            }

            resp.put("ok", true);
            return ResponseEntity.ok(resp);

        } catch (Exception ex) {
            resp.put("ok", false);
            resp.put("error", ex.getMessage());
            return ResponseEntity.internalServerError().body(resp);
        }
    }
}