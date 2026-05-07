package com.daw.adoptauncompanero.controladores.rest;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.daw.adoptauncompanero.dtos.AnimalDTO;
import com.daw.adoptauncompanero.dtos.CitaDTO;
import com.daw.adoptauncompanero.dtos.EstadisticasDTO;
import com.daw.adoptauncompanero.dtos.FavoritoDTO;
import com.daw.adoptauncompanero.dtos.HistorialEstadoDTO;
import com.daw.adoptauncompanero.dtos.SolicitudDTO;
import com.daw.adoptauncompanero.dtos.UsuarioDTO;
import com.daw.adoptauncompanero.entities.AnimalEntity;
import com.daw.adoptauncompanero.entities.UsuarioEntity;
import com.daw.adoptauncompanero.repositorios.ImagenAnimalRepository;
import org.springframework.web.multipart.MultipartFile;
import com.daw.adoptauncompanero.entities.ImagenAnimalEntity;
import com.daw.adoptauncompanero.servicio.interfaces.*;

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

	// ============ ANIMALES ============

	// Listado paginado de animales (público)
	@GetMapping("/animales")
	public ResponseEntity<Page<AnimalDTO>> listarAnimalesPaginados(
			@PageableDefault(size = 8, sort = "idAnimal") Pageable pageable) {
		return ResponseEntity.ok(animalService.listarAnimalesPaginados(pageable));
	}

	// Búsqueda con filtros
	@GetMapping("/animales/buscar")
	public ResponseEntity<List<AnimalDTO>> buscar(@RequestParam(required = false) String nombre,
			@RequestParam(required = false) String especie, @RequestParam(required = false) Integer edadMin,
			@RequestParam(required = false) Integer edadMax, @RequestParam(required = false) String tamano,
			@RequestParam(required = false) String estado) {
		return ResponseEntity.ok(animalService.buscarAnimales(null, nombre, especie, edadMin, edadMax, tamano, estado));
	}

	// Ficha de un animal individual
	@GetMapping("/animales/{id}")
	public ResponseEntity<Map<String, Object>> obtenerAnimal(@PathVariable Integer id) {
		AnimalEntity a = animalService.obtenerAnimalPorId(id);
		if (a == null)
			return ResponseEntity.notFound().build();

		Map<String, Object> respuesta = new HashMap<>();
		respuesta.put("idAnimal", a.getIdAnimal());
		respuesta.put("nombre", a.getNombre());
		respuesta.put("especie", a.getEspecie());
		respuesta.put("edad", a.getEdad());
		respuesta.put("tamano", a.getTamano());
		respuesta.put("personalidad", a.getPersonalidad());
		respuesta.put("necesidadesEspeciales", a.getNecesidadesEspeciales());
		respuesta.put("estadoSanitario", a.getEstadoSanitario());
		respuesta.put("estado", a.getEstado().name());

		// Lista de imágenes (solo nombres)
		List<Map<String, Object>> imgs = a.getImagenes() == null ? List.of() : a.getImagenes().stream().map(i -> {
			Map<String, Object> m = new HashMap<>();
			m.put("idImagen", i.getIdImagen());
			m.put("urlImagen", i.getUrlImagen());
			return m;
		}).toList();
		respuesta.put("imagenes", imgs);

		return ResponseEntity.ok(respuesta);
	}

	// Crear animal (admin)
	@PostMapping("/animales")
	public ResponseEntity<Integer> crear(@RequestBody Map<String, Object> body) {
		Integer id = animalService.insertarAnimal((String) body.get("nombre"), (String) body.get("especie"),
				(Integer) body.get("edad"), (String) body.get("tamano"), (String) body.get("personalidad"),
				(String) body.get("necesidadesEspeciales"), (String) body.get("estadoSanitario"),
				(String) body.getOrDefault("estado", "DISPONIBLE"));
		return ResponseEntity.ok(id);
	}

	// Actualizar animal
	@PutMapping("/animales/{id}")
	public ResponseEntity<Integer> actualizar(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
		Integer res = animalService.actualizarAnimal(id, (String) body.get("nombre"), (String) body.get("especie"),
				(Integer) body.get("edad"), (String) body.get("tamano"), (String) body.get("personalidad"),
				(String) body.get("necesidadesEspeciales"), (String) body.get("estadoSanitario"),
				(String) body.get("estado"));
		return ResponseEntity.ok(res);
	}

	// Borrar animal
	@DeleteMapping("/animales/{id}")
	public ResponseEntity<Integer> borrar(@PathVariable Integer id) {
		return ResponseEntity.ok(animalService.borrarAnimal(id));
	}

	// ============ AUTENTICACIÓN ============

	// Devuelve los datos del usuario logueado (o null)
	@GetMapping("/me")
	public ResponseEntity<Map<String, Object>> miSesion(Principal principal) {
		if (principal == null) {
			return ResponseEntity.ok(Map.of("autenticado", false));
		}
		UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());
		if (u == null)
			return ResponseEntity.ok(Map.of("autenticado", false));

		String rol = u.getUsuarioRoles().stream().findFirst().map(ur -> ur.getRol().getNombre()).orElse("CLIENTE");

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

	// ============ SOLICITUDES ============

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
	public ResponseEntity<List<HistorialEstadoDTO>> historial(@PathVariable Integer id) {
		return ResponseEntity.ok(solicitudService.obtenerHistorial(id));
	}

	@PostMapping("/solicitudes")
	public ResponseEntity<Integer> iniciar(@RequestBody Map<String, Object> body, Principal principal) {
		UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());

		Integer idAnimal = Integer.parseInt(body.get("idAnimal").toString());
		String comentarios = body.get("comentarios") != null ? body.get("comentarios").toString() : "";

		Integer res = solicitudService.iniciarSolicitud(u.getIdUsuario(), idAnimal, comentarios);
		return ResponseEntity.ok(res);
	}

	@PutMapping("/solicitudes/{id}/estado")
	public ResponseEntity<Integer> cambiarEstado(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
		return ResponseEntity.ok(solicitudService.cambiarEstado(id, (Integer) body.get("idEstadoNuevo"),
				(String) body.get("comentarioAdmin")));
	}

	@DeleteMapping("/solicitudes/{id}")
	public ResponseEntity<Integer> cancelar(@PathVariable Integer id, Principal principal) {
		UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());
		return ResponseEntity.ok(solicitudService.cancelarSolicitud(id, u.getIdUsuario()));
	}

	// ============ FAVORITOS ============

	@GetMapping("/favoritos")
	public ResponseEntity<List<FavoritoDTO>> misFavoritos(Principal principal) {
		UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());
		return ResponseEntity.ok(favoritoService.listarFavoritosPorUsuario(u.getIdUsuario()));
	}

	@PostMapping("/favoritos/{idAnimal}")
	public ResponseEntity<Integer> agregar(@PathVariable Integer idAnimal, Principal principal) {
		UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());
		return ResponseEntity.ok(favoritoService.agregarFavorito(u.getIdUsuario(), idAnimal));
	}

	@DeleteMapping("/favoritos/{idAnimal}")
	public ResponseEntity<Integer> quitar(@PathVariable Integer idAnimal, Principal principal) {
		UsuarioEntity u = usuarioService.obtenerPorEmail(principal.getName());
		return ResponseEntity.ok(favoritoService.quitarFavorito(u.getIdUsuario(), idAnimal));
	}

	// ============ USUARIOS (admin) ============

	@GetMapping("/usuarios")
	public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
		return ResponseEntity.ok(usuarioService.buscarUsuarios(null, null, null));
	}

	@PutMapping("/usuarios/{id}")
	public ResponseEntity<Integer> actualizarMisDatos(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
		return ResponseEntity.ok(usuarioService.actualizarDatosPersonales(id, (String) body.get("nombre"),
				(String) body.get("telefono"), (String) body.get("direccion")));
	}

	@DeleteMapping("/usuarios/{id}")
	public ResponseEntity<Integer> borrarUsuario(@PathVariable Integer id) {
		return ResponseEntity.ok(usuarioService.borrarUsuario(id));
	}

	// ============ CITAS (admin) ============

	@GetMapping("/citas")
	public ResponseEntity<List<CitaDTO>> citas() {
		return ResponseEntity.ok(citaService.listarTodasLasCitas());
	}

	@PostMapping("/citas")
	public ResponseEntity<Integer> programarCita(@RequestBody Map<String, Object> body) {
		return ResponseEntity.ok(citaService.programarCita((Integer) body.get("idSolicitud"),
				java.time.LocalDateTime.parse((String) body.get("fechaCita")), (String) body.get("observaciones")));
	}

	@DeleteMapping("/citas/{id}")
	public ResponseEntity<Integer> borrarCita(@PathVariable Integer id) {
		return ResponseEntity.ok(citaService.borrarCita(id));
	}

	// ============ ESTADÍSTICAS ============

	@GetMapping("/estadisticas")
	public ResponseEntity<EstadisticasDTO> estadisticas() {
		return ResponseEntity.ok(estadisticasService.calcularEstadisticas());
	}

	// ============ IMÁGENES DE ANIMALES (PDF 6.2 - Almacenamiento) ============

	// Listar todas las imágenes de un animal
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

	// Subir imagen para un animal (multipart/form-data)
	// Reusa la lógica del FileStorageService (PDF 6.2)
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

	// Borrar una imagen
	@DeleteMapping("/animales/imagenes/{idImagen}")
	public ResponseEntity<Map<String, Object>> borrarImagenAnimal(@PathVariable Integer idImagen) {
		Map<String, Object> resp = new HashMap<>();
		Integer res = animalService.borrarImagen(idImagen);
		if (res == 0) {
			resp.put("ok", false);
			resp.put("error", "Imagen no encontrada");
			return ResponseEntity.notFound().build();
		}
		resp.put("ok", true);
		return ResponseEntity.ok(resp);
	}
	// ============ REGISTRO PÚBLICO ============

	@PostMapping("/registro")
	public ResponseEntity<Map<String, Object>> registrarCliente(@RequestBody Map<String, Object> body) {

		Integer res = usuarioService.registrarCliente((String) body.get("nombre"), (String) body.get("email"),
				(String) body.get("password"), (String) body.get("telefono"), (String) body.get("direccion"));

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

}
