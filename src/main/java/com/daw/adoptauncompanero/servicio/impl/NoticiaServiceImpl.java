package com.daw.adoptauncompanero.servicio.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.daw.adoptauncompanero.dtos.NoticiaDTO;
import com.daw.adoptauncompanero.entities.NoticiaEntity;
import com.daw.adoptauncompanero.repositorios.NoticiaRepository;
import com.daw.adoptauncompanero.servicio.interfaces.FileStorageService;
import com.daw.adoptauncompanero.servicio.interfaces.NoticiaService;

@Service
public class NoticiaServiceImpl implements NoticiaService {

	@Autowired
	private NoticiaRepository noticiaRepository;
	@Autowired
	private FileStorageService fileStorageService;

	@Override
	public List<NoticiaDTO> listarPublicadas() {
		return noticiaRepository.listarNoticiasPublicadas();
	}

	@Override
	public List<NoticiaDTO> listarTodas() {
		return noticiaRepository.listarTodasNoticias();
	}

	@Override
	public List<NoticiaDTO> listarDestacadas(int cantidad) {
		return noticiaRepository.listarDestacadas(PageRequest.of(0, cantidad));
	}

	@Override
	public NoticiaEntity obtenerPorId(Integer idNoticia) {
		return noticiaRepository.findById(idNoticia).orElse(null);
	}

	@Override
	@Transactional
	public Integer crearNoticia(String titulo, String subtitulo, String contenido, String autor, Boolean publicada) {
		if (titulo == null || titulo.isBlank() || contenido == null || contenido.isBlank()) {
			return -1;
		}
		NoticiaEntity n = new NoticiaEntity();
		n.setTitulo(titulo);
		n.setSubtitulo(subtitulo);
		n.setContenido(contenido);
		n.setAutor(autor);
		n.setPublicada(publicada == null ? true : publicada);
		n.setFechaPublicacion(LocalDateTime.now());
		n.setFechaModificacion(LocalDateTime.now());
		return noticiaRepository.save(n).getIdNoticia();
	}

	@Override
	@Transactional
	public Integer actualizarNoticia(Integer idNoticia, String titulo, String subtitulo, String contenido, String autor,
			Boolean publicada) {
		NoticiaEntity n = noticiaRepository.findById(idNoticia).orElse(null);
		if (n == null)
			return 0;
		if (titulo != null && !titulo.isBlank())
			n.setTitulo(titulo);
		n.setSubtitulo(subtitulo);
		if (contenido != null && !contenido.isBlank())
			n.setContenido(contenido);
		n.setAutor(autor);
		if (publicada != null)
			n.setPublicada(publicada);
		n.setFechaModificacion(LocalDateTime.now());
		noticiaRepository.save(n);
		return idNoticia;
	}

	@Override
	@Transactional
	public Integer borrarNoticia(Integer idNoticia) {
		if (!noticiaRepository.existsById(idNoticia))
			return 0;
		noticiaRepository.deleteById(idNoticia);
		return idNoticia;
	}

	@Override
	@Transactional
	public Integer subirImagenNoticia(Integer idNoticia, MultipartFile archivo) {
		NoticiaEntity n = noticiaRepository.findById(idNoticia).orElse(null);
		if (n == null)
			return 0;
		String nombreArchivo = fileStorageService.guardarArchivo(archivo);
		n.setImagen(nombreArchivo);
		n.setFechaModificacion(LocalDateTime.now());
		noticiaRepository.save(n);
		return idNoticia;
	}
}