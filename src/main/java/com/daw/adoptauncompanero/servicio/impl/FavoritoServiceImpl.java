package com.daw.adoptauncompanero.servicio.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.adoptauncompanero.dtos.FavoritoDTO;
import com.daw.adoptauncompanero.entities.AnimalEntity;
import com.daw.adoptauncompanero.entities.FavoritoEntity;
import com.daw.adoptauncompanero.entities.FavoritoId;
import com.daw.adoptauncompanero.entities.UsuarioEntity;
import com.daw.adoptauncompanero.repositorios.AnimalRepository;
import com.daw.adoptauncompanero.repositorios.FavoritoRepository;
import com.daw.adoptauncompanero.repositorios.UsuarioRepository;
import com.daw.adoptauncompanero.servicio.interfaces.FavoritoService;

@Service
public class FavoritoServiceImpl implements FavoritoService {

	@Autowired
	private FavoritoRepository favoritoRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private AnimalRepository animalRepository;

	@Override
	@Transactional
	public Integer agregarFavorito(Integer idUsuario, Integer idAnimal) {
		UsuarioEntity u = usuarioRepository.findById(idUsuario).orElse(null);
		AnimalEntity a = animalRepository.findById(idAnimal).orElse(null);
		if (u == null || a == null)
			return 0;

		if (favoritoRepository.contarFavorito(idUsuario, idAnimal) > 0)
			return 1;

		FavoritoEntity f = new FavoritoEntity(u, a);
		favoritoRepository.save(f);
		return 1;
	}

	@Override
	@Transactional
	public Integer quitarFavorito(Integer idUsuario, Integer idAnimal) {
		FavoritoId id = new FavoritoId(idUsuario, idAnimal);
		if (!favoritoRepository.existsById(id))
			return 0;
		favoritoRepository.deleteById(id);
		return 1;
	}

	@Override
	public List<FavoritoDTO> listarFavoritosPorUsuario(Integer idUsuario) {
		return favoritoRepository.listarFavoritosPorUsuario(idUsuario);
	}

	@Override
	public Boolean esFavorito(Integer idUsuario, Integer idAnimal) {
		return favoritoRepository.contarFavorito(idUsuario, idAnimal) > 0;
	}
}
