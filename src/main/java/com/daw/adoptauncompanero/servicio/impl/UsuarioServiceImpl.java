package com.daw.adoptauncompanero.servicio.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.adoptauncompanero.dtos.UsuarioDTO;
import com.daw.adoptauncompanero.entities.RolEntity;
import com.daw.adoptauncompanero.entities.UsuarioEntity;
import com.daw.adoptauncompanero.entities.UsuarioRolEntity;
import com.daw.adoptauncompanero.repositorios.RolRepository;
import com.daw.adoptauncompanero.repositorios.UsuarioRepository;
import com.daw.adoptauncompanero.servicio.interfaces.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private RolRepository rolRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public Integer registrarCliente(String nombre, String email, String password, String telefono, String direccion) {

		if (usuarioRepository.contarPorEmail(email) > 0)
			return -1;

		UsuarioEntity u = new UsuarioEntity();
		u.setNombre(nombre);
		u.setEmail(email);
		u.setPassword(passwordEncoder.encode(password));
		u.setTelefono(telefono);
		u.setDireccion(direccion);
		u.setFechaRegistro(LocalDateTime.now());

		RolEntity rolCliente = rolRepository.findByNombre("CLIENTE");
		Set<UsuarioRolEntity> roles = new HashSet<>();
		UsuarioRolEntity ur = new UsuarioRolEntity(u, rolCliente);
		roles.add(ur);
		u.setUsuarioRoles(roles);

		return usuarioRepository.save(u).getIdUsuario();
	}

	@Override
	public UsuarioEntity obtenerPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	@Override
	public UsuarioEntity obtenerPorId(Integer id) {
		return usuarioRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Integer actualizarDatosPersonales(Integer id, String nombre, String telefono, String direccion) {
		UsuarioEntity u = usuarioRepository.findById(id).orElse(null);
		if (u == null)
			return 0;

		u.setNombre(nombre);
		u.setTelefono(telefono);
		u.setDireccion(direccion);

		return usuarioRepository.save(u).getIdUsuario();
	}

	@Override
	public List<UsuarioDTO> buscarUsuarios(Integer id, String nombre, String email) {
		String n = (nombre != null && !nombre.trim().isEmpty()) ? nombre.trim() : null;
		String e = (email != null && !email.trim().isEmpty()) ? email.trim() : null;
		return usuarioRepository.buscarUsuariosPorFiltros(id, n, e);
	}

	@Override
	public Integer borrarUsuario(Integer id) {
		if (!usuarioRepository.existsById(id))
			return 0;
		usuarioRepository.deleteById(id);
		return id;
	}
}
