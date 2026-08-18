package com.entysoftware.aplication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String ROL_ADMINISTRADOR = "ADMINISTRADOR";
    private static final String ROL_ASISTENTE = "ASISTENTE";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/buscar-establecimiento/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/health").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // Tareas de mesero: administrador y asistente pueden gestionar pedidos y consultar mesas
                        .requestMatchers("/pedidos/**").hasAnyRole(ROL_ADMINISTRADOR, ROL_ASISTENTE)
                        .requestMatchers(HttpMethod.GET, "/mesas/listar-mesas/**").hasAnyRole(ROL_ADMINISTRADOR, ROL_ASISTENTE)
                        .requestMatchers(HttpMethod.GET, "/caja/establecimiento-estado-caja/**").hasAnyRole(ROL_ADMINISTRADOR, ROL_ASISTENTE)
                        // Gestión del establecimiento (mesas, caja, cierre): solo administrador
                        .anyRequest().hasRole(ROL_ADMINISTRADOR)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
