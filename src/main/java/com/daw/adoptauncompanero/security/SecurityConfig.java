package com.daw.adoptauncompanero.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// =============================================================
// PDF 5 - 2.5. Configuración de Spring Security
// Reglas:
//  - Cliente NO registrado (2.2.1): home, catálogo, ficha animal,
//    info adopción, registro, login. Sin más permisos.
//  - Cliente REGISTRADO / CLIENTE (2.2.3): área personal, favoritos,
//    solicitudes propias.
//  - ADMIN (2.2.4): gestión completa (animales, usuarios, solicitudes,
//    citas, estadísticas).
// =============================================================
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private DetallesUsuarioService userDetailsService;

    /**
     * BCrypt es el encoder recomendado por Spring (PDF 5).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cómo se autentican los usuarios (BBDD + BCrypt).
     * IMPORTANTE: en Spring Security 6.3+ se usa el constructor vacío
     * y se inyecta el UserDetailsService con el setter.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Reglas de autorización por URL.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // ---------- URLs públicas (cliente no registrado, 2.2.1) ----------
                .requestMatchers(
                        "/", "/home", "/login", "/registro", "/accesoDenegado",
                        "/css/**", "/js/**", "/img/**", "/uploads/**"
                ).permitAll()

                // Catálogo y fichas son públicos (2.3.1, 2.3.2)
                .requestMatchers("/animales/catalogo", "/animales/ficha/**").permitAll()

                // API REST de animales pública (paginación PDF 6.3)
                .requestMatchers("/v1/animalesPaginacion/**").permitAll()

                // ---------- Zona ADMIN (2.2.4 / 2.3.5) ----------
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/animales/insertar/**", "/animales/actualizar/**",
                                  "/animales/borrar/**", "/animales/imagenes/**").hasAuthority("ADMIN")
                .requestMatchers("/usuarios/listado/**", "/usuarios/borrar/**").hasAuthority("ADMIN")
                .requestMatchers("/citas/**").hasAuthority("ADMIN")
                .requestMatchers("/estadisticas/**").hasAuthority("ADMIN")

                // Cambio de estado de solicitudes (admin)
                .requestMatchers("/solicitudes/cambiarEstado/**",
                                 "/solicitudes/listadoAdmin/**").hasAuthority("ADMIN")

                // ---------- Zona CLIENTE registrado (2.2.3) ----------
                .requestMatchers("/area-personal/**").hasAnyAuthority("CLIENTE", "ADMIN")
                .requestMatchers("/favoritos/**").hasAnyAuthority("CLIENTE", "ADMIN")
                .requestMatchers("/solicitudes/iniciar/**",
                                 "/solicitudes/misSolicitudes/**",
                                 "/solicitudes/cancelar/**").hasAnyAuthority("CLIENTE", "ADMIN")

                // Resto requiere estar autenticado
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/home", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .exceptionHandling(exception -> exception.accessDeniedPage("/accesoDenegado"));

        return http.build();
    }
}