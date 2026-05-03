package com.daw.adoptauncompanero.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.daw.adoptauncompanero.entities.UsuarioEntity;
import com.daw.adoptauncompanero.entities.UsuarioRolEntity;
import com.daw.adoptauncompanero.repositorios.UsuarioRepository;

// =============================================================
// PDF 5 - 2.4. UserDetailsService
// Spring Security llama a este servicio para cargar los datos
// del usuario al hacer login. Aquí transformamos nuestros roles
// (CLIENTE / ADMIN) en GrantedAuthority.
// =============================================================
@Service
public class DetallesUsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 1. Buscar usuario por email (nuestro "username")
        UsuarioEntity user = usuarioRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }

        // 2. Convertir los roles a GrantedAuthority (CLIENTE -> ROLE_CLIENTE, ADMIN -> ROLE_ADMIN)
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (UsuarioRolEntity ur : user.getUsuarioRoles()) {
            authorities.add(new SimpleGrantedAuthority(ur.getRol().getNombre()));
        }

        // 3. Devolver objeto UserDetails de Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}