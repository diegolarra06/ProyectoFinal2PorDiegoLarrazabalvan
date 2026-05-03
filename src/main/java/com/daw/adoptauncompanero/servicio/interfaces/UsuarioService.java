package com.daw.adoptauncompanero.servicio.interfaces;

import java.util.List;

import com.daw.adoptauncompanero.dtos.UsuarioDTO;
import com.daw.adoptauncompanero.entities.UsuarioEntity;

public interface UsuarioService {

    // Registro nuevo cliente (2.2.1.5)
    Integer registrarCliente(String nombre, String email, String password,
                             String telefono, String direccion);

    // 2.3.3.1 - área personal: ver perfil
    UsuarioEntity obtenerPorEmail(String email);

    UsuarioEntity obtenerPorId(Integer id);

    // 2.3.3.1 - editar datos de contacto
    Integer actualizarDatosPersonales(Integer id, String nombre, String telefono, String direccion);

    // 2.2.4.6 - admin lista todos los usuarios
    List<UsuarioDTO> buscarUsuarios(Integer id, String nombre, String email);

    // 2.2.4.6 - admin elimina un usuario
    Integer borrarUsuario(Integer id);
}