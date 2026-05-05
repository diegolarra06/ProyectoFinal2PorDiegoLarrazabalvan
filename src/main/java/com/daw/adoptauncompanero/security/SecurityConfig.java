package com.daw.adoptauncompanero.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

// =============================================================
// PDF 5 - 2.5. Configuración de Spring Security
// Adaptado para SPA Vue: el login devuelve 200 OK (no redirige)
// y el access denied devuelve 401/403 (no redirige a HTML).
// =============================================================
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private DetallesUsuarioService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .authorizeHttpRequests(auth -> auth

                // ---------- URLs públicas ----------
                .requestMatchers(
                        "/", "/home", "/login", "/registro", "/accesoDenegado",
                        "/css/**", "/js/**", "/img/**", "/uploads/**",
                        "/api/animales/**", "/api/me", "/api/registro"
                ).permitAll()

                // ---------- Zona ADMIN ----------
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/animales/insertar/**", "/animales/actualizar/**",
                                  "/animales/borrar/**", "/animales/imagenes/**").hasAuthority("ADMIN")
                .requestMatchers("/usuarios/listado/**", "/usuarios/borrar/**").hasAuthority("ADMIN")
                .requestMatchers("/citas/**").hasAuthority("ADMIN")
                .requestMatchers("/estadisticas/**").hasAuthority("ADMIN")
                .requestMatchers("/solicitudes/cambiarEstado/**",
                                 "/solicitudes/listadoAdmin/**").hasAuthority("ADMIN")

                // ---------- Endpoints REST autenticados ----------
                .requestMatchers("/api/**").authenticated()

                // ---------- Zona CLIENTE registrado ----------
                .requestMatchers("/area-personal/**").hasAnyAuthority("CLIENTE", "ADMIN")
                .requestMatchers("/favoritos/**").hasAnyAuthority("CLIENTE", "ADMIN")
                .requestMatchers("/solicitudes/iniciar/**",
                                 "/solicitudes/misSolicitudes/**",
                                 "/solicitudes/cancelar/**").hasAnyAuthority("CLIENTE", "ADMIN")

                .anyRequest().authenticated()
            )
            // Login configurado para SPA: devuelve 200 OK (no redirige a HTML)
            .formLogin(form -> form
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                // Si login OK → 200 OK (sin redirect)
                .successHandler((req, resp, auth) -> {
                    resp.setStatus(HttpStatus.OK.value());
                    resp.setContentType("application/json");
                    resp.getWriter().write("{\"ok\":true}");
                })
                // Si login KO → 401 Unauthorized
                .failureHandler((req, resp, ex) -> {
                    resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                    resp.setContentType("application/json");
                    resp.getWriter().write("{\"ok\":false,\"error\":\"Credenciales incorrectas\"}");
                })
                .permitAll()
            )
            // Logout SPA: devuelve 200 OK (sin redirect)
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler((req, resp, auth) -> {
                    resp.setStatus(HttpStatus.OK.value());
                    resp.setContentType("application/json");
                    resp.getWriter().write("{\"ok\":true}");
                })
                .permitAll()
            )
            // Si una petición REST no está autenticada → 401 (sin redirect a HTML)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            );

        return http.build();
    }
}