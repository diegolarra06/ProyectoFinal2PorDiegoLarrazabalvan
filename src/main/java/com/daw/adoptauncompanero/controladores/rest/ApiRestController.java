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

	private Integer toInteger(Object valor) {
		if (valor == null)
			return null;
		if (valor instanceof Integer)
			return (Integer) valor;
		if (valor instanceof Number)
			return ((Number) valor).intValue();

		String texto = valor.toString().trim();
		if (texto.isEmpty())
			return null;

		return Integer.parseInt(texto);
	}

	private Boolean toBoolean(Object valor, Boolean valorPorDefecto) {
		if (valor == null)
			return valorPorDefecto;
		if (valor instanceof Boolean)
			return (Boolean) valor;

		String texto = valor.toString().trim();
		if (texto.isEmpty())
			return valorPorDefecto;

		return Boolean.parseBoolean(texto);
	}

	private String toStringOrNull(Object valor) {
		if (valor == null)
			return null;
		String texto = valor.toString().trim();
		return texto.isEmpty() ? null : texto;
	}

	@GetMapping("/animales")
	public ResponseEntity<Page<AnimalDTO>> listarAnimalesPaginados(
			@PageableDefault(size = 8, sort = "idAnimal") Pageable pageable) {

		Page<AnimalDTO> pagina = animalService.listarAnimalesPaginados(pageable);
		return ResponseEntity.ok(pagina);
	}

	@GetMapping("/animales/especies")
	public ResponseEntity<List<String>> listarEspecies() {
		return ResponseEntity.ok(animalService.listarEspeciesDistintas());
	}

	@GetMapping("/animales/buscar")
	public ResponseEntity<List<AnimalDTO>> buscarAnimales(@RequestParam(required = false) String nombre,
			@RequestParam(required = false) String especie, @RequestParam(required = false) Integer edadMin,
			@RequestParam(required = false) Integer edadMax, @RequestParam(required = false) String tamano,
			@RequestParam(required = false) String estado) {

		List<AnimalDTO> animales = animalService.buscarAnimales(null, nombre, especie, edadMin, edadMax, tamano,
				estado);

		return ResponseEntity.ok(animales);
	}

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

		List<Map<String, Object>> imagenes = a.getImagenes() == null ? List.of() : a.getImagenes().stream().map(i -> {
			Map<String, Object> m = new HashMap<>();
			m.put("idImagen", i.getIdImagen());
			m.put("urlImagen", i.getUrlImagen());
			m.put("urlCompleta", "http://localhost:8080/uploads/" + i.getUrlImagen());
			return m;
		}).toList();

		respuesta.put("imagenes", imagenes);

		return ResponseEntity.ok(respuesta);
	}

	@PostMapping("/animales")
	public ResponseEntity<Integer> crearAnimal(@RequestBody Map<String, Object> body) {
		Integer id = animalService.insertarAnimal(toStringOrNull(body.get("nombre")),
				toStringOrNull(body.get("especie")), toInteger(body.get("edad")), toStringOrNull(body.get("tamano")),
				toStringOrNull(body.get("personalidad")), toStringOrNull(body.get("necesidadesEspeciales")),
				toStringOrNull(body.get("estadoSanitario")), toStringOrNull(body.getOrDefault("estado", "DISPONIBLE")));

		return ResponseEntity.ok(id);
	}

	@PutMapping("/animales/{id}")
	public ResponseEntity<Integer> actualizarAnimal(@PathVariable Integer id, @RequestBody Map<String, Object> body) {

		Integer res = animalService.actualizarAnimal(id, toStringOrNull(body.get("nombre")),
				toStringOrNull(body.get("especie")), toInteger(body.get("edad")), toStringOrNull(body.get("tamano")),
				toStringOrNull(body.get("personalidad")), toStringOrNull(body.get("necesidadesEspeciales")),
				toStringOrNull(body.get("estadoSanitario")), toStringOrNull(body.get("estado")));

		return ResponseEntity.ok(res);
	}

	@DeleteMapping("/animales/{id}")
	public ResponseEntity<Integer> borrarAnimal(@PathVariable Integer id) {
		return ResponseEntity.ok(animalService.borrarAnimal(id));
	}

	@GetMapping("/me")
	public ResponseEntity<Map<String, Object>> miSesion(Principal principal) {
		if (principal == null) {
			return ResponseEntity.ok(Map.of("autenticado", false));
		}

		UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());

		if (u == null) {
			return ResponseEntity.ok(Map.of("autenticado", false));
		}

		String rol = u.getUsuarioRoles() == null ? "CLIENTE"
				: u.getUsuarioRoles().stream().findFirst().map(ur -> ur.getRol().getNombre()).orElse("CLIENTE");

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

	@PostMapping("/registro")
	public ResponseEntity<Map<String, Object>> registrarCliente(@RequestBody Map<String, Object> body) {
		Integer res = usuarioService.registrarCliente(toStringOrNull(body.get("nombre")),
				toStringOrNull(body.get("email")), toStringOrNull(body.get("password")),
				toStringOrNull(body.get("telefono")), toStringOrNull(body.get("direccion")));

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

	@GetMapping("/solicitudes/mias")
	public ResponseEntity<List<SolicitudDTO>> misSolicitudes(Principal principal) {
		UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());
		return ResponseEntity.ok(solicitudService.listarSolicitudesPorUsuario(u.getIdUsuario()));
	}

	@GetMapping("/solicitudes")
	public ResponseEntity<List<SolicitudDTO>> todasSolicitudes() {
		return ResponseEntity.ok(solicitudService.buscarSolicitudes(null, null, null, null));
	}

	@GetMapping("/solicitudes/{id}/historial")
	public ResponseEntity<List<HistorialEstadoDTO>> historialSolicitud(@PathVariable Integer id) {
		return ResponseEntity.ok(solicitudService.obtenerHistorial(id));
	}

	@PostMapping("/solicitudes")
	public ResponseEntity<Integer> iniciarSolicitud(@RequestBody Map<String, Object> body, Principal principal) {

		UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());

		Integer idAnimal = toInteger(body.get("idAnimal"));
		String comentarios = toStringOrNull(body.get("comentarios"));
		if (comentarios == null)
			comentarios = "";

		Integer res = solicitudService.iniciarSolicitud(u.getIdUsuario(), idAnimal, comentarios);

		return ResponseEntity.ok(res);
	}

	@PutMapping("/solicitudes/{id}/estado")
	public ResponseEntity<Integer> cambiarEstadoSolicitud(@PathVariable Integer id,
			@RequestBody Map<String, Object> body) {

		Integer idEstadoNuevo = toInteger(body.get("idEstadoNuevo"));
		String comentarioAdmin = toStringOrNull(body.get("comentarioAdmin"));

		Integer res = solicitudService.cambiarEstado(id, idEstadoNuevo, comentarioAdmin);

		return ResponseEntity.ok(res);
	}

	@DeleteMapping("/solicitudes/{id}")
	public ResponseEntity<Integer> cancelarSolicitud(@PathVariable Integer id, Principal principal) {

		UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());
		Integer res = solicitudService.cancelarSolicitud(id, u.getIdUsuario());

		return ResponseEntity.ok(res);
	}

	@GetMapping("/favoritos")
	public ResponseEntity<List<FavoritoDTO>> misFavoritos(Principal principal) {
		UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());
		return ResponseEntity.ok(favoritoService.listarFavoritosPorUsuario(u.getIdUsuario()));
	}

	@PostMapping("/favoritos/{idAnimal}")
	public ResponseEntity<Integer> agregarFavorito(@PathVariable Integer idAnimal, Principal principal) {

		UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());
		return ResponseEntity.ok(favoritoService.agregarFavorito(u.getIdUsuario(), idAnimal));
	}

	@DeleteMapping("/favoritos/{idAnimal}")
	public ResponseEntity<Integer> quitarFavorito(@PathVariable Integer idAnimal, Principal principal) {

		UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());
		return ResponseEntity.ok(favoritoService.quitarFavorito(u.getIdUsuario(), idAnimal));
	}

	@GetMapping("/usuarios")
	public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
		return ResponseEntity.ok(usuarioService.buscarUsuarios(null, null, null));
	}

	@PutMapping("/usuarios/{id}")
	public ResponseEntity<Integer> actualizarMisDatos(@PathVariable Integer id, @RequestBody Map<String, Object> body) {

		Integer res = usuarioService.actualizarDatosPersonales(id, toStringOrNull(body.get("nombre")),
				toStringOrNull(body.get("telefono")), toStringOrNull(body.get("direccion")));

		return ResponseEntity.ok(res);
	}

	@DeleteMapping("/usuarios/{id}")
	public ResponseEntity<Integer> borrarUsuario(@PathVariable Integer id) {
		return ResponseEntity.ok(usuarioService.borrarUsuario(id));
	}

	// =============================================================
	// CITAS
	// =============================================================

	@GetMapping("/citas")
	public ResponseEntity<List<CitaDTO>> listarCitas() {
		return ResponseEntity.ok(citaService.listarTodasLasCitas());
	}

	@PostMapping("/citas")
	public ResponseEntity<Integer> programarCita(@RequestBody Map<String, Object> body) {
		Integer idSolicitud = toInteger(body.get("idSolicitud"));
		LocalDateTime fechaCita = LocalDateTime.parse(toStringOrNull(body.get("fechaCita")));
		String observaciones = toStringOrNull(body.get("observaciones"));

		Integer res = citaService.programarCita(idSolicitud, fechaCita, observaciones);

		return ResponseEntity.ok(res);
	}

	@DeleteMapping("/citas/{id}")
	public ResponseEntity<Integer> borrarCita(@PathVariable Integer id) {
		return ResponseEntity.ok(citaService.borrarCita(id));
	}

	@GetMapping("/estadisticas")
	public ResponseEntity<EstadisticasDTO> estadisticas() {
		return ResponseEntity.ok(estadisticasService.calcularEstadisticas());
	}

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

	@PostMapping("/animales/{idAnimal}/imagenes")
	public ResponseEntity<Map<String, Object>> subirImagenAnimal(@PathVariable Integer idAnimal,
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

	@GetMapping("/noticias")
	public ResponseEntity<List<NoticiaDTO>> listarNoticias() {
		return ResponseEntity.ok(noticiaService.listarPublicadas());
	}

	@GetMapping("/noticias/destacadas")
	public ResponseEntity<List<NoticiaDTO>> noticiasDestacadas(@RequestParam(defaultValue = "5") int cantidad) {

		return ResponseEntity.ok(noticiaService.listarDestacadas(cantidad));
	}

	@GetMapping("/noticias/admin")
	public ResponseEntity<List<NoticiaDTO>> listarTodasNoticiasAdmin() {
		return ResponseEntity.ok(noticiaService.listarTodas());
	}

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

	@PostMapping("/noticias")
	public ResponseEntity<Integer> crearNoticia(@RequestBody Map<String, Object> body) {
		Integer id = noticiaService.crearNoticia(toStringOrNull(body.get("titulo")),
				toStringOrNull(body.get("subtitulo")), toStringOrNull(body.get("contenido")),
				toStringOrNull(body.get("autor")), toBoolean(body.get("publicada"), true));

		return ResponseEntity.ok(id);
	}

	@PutMapping("/noticias/{id}")
	public ResponseEntity<Integer> actualizarNoticia(@PathVariable Integer id, @RequestBody Map<String, Object> body) {

		Integer res = noticiaService.actualizarNoticia(id, toStringOrNull(body.get("titulo")),
				toStringOrNull(body.get("subtitulo")), toStringOrNull(body.get("contenido")),
				toStringOrNull(body.get("autor")), toBoolean(body.get("publicada"), true));

		return ResponseEntity.ok(res);
	}

	@DeleteMapping("/noticias/{id}")
	public ResponseEntity<Integer> borrarNoticia(@PathVariable Integer id) {
		return ResponseEntity.ok(noticiaService.borrarNoticia(id));
	}

	@PostMapping("/noticias/{id}/imagen")
	public ResponseEntity<Map<String, Object>> subirImagenNoticia(@PathVariable Integer id,
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