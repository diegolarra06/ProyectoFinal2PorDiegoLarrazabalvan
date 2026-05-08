package com.daw.adoptauncompanero.servicio.interfaces;

import java.util.List;

import com.daw.adoptauncompanero.dtos.UsuarioDTO;
import com.daw.adoptauncompanero.entities.UsuarioEntity;

public interface UsuarioService {

	Integer registrarCliente(String nombre, String email, String password, String telefono, String direccion);

	UsuarioEntity obtenerPorEmail(String email);

	UsuarioEntity obtenerPorId(Integer id);

	Integer actualizarDatosPersonales(Integer id, String nombre, String telefono, String direccion);

	List<UsuarioDTO> buscarUsuarios(Integer id, String nombre, String email);

	Integer borrarUsuario(Integer id);
}