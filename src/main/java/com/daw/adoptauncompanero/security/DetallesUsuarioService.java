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

@Service
public class DetallesUsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		UsuarioEntity user = usuarioRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("Usuario no encontrado: " + email);
		}

		Set<GrantedAuthority> authorities = new HashSet<>();
		for (UsuarioRolEntity ur : user.getUsuarioRoles()) {
			authorities.add(new SimpleGrantedAuthority(ur.getRol().getNombre()));
		}

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}
}