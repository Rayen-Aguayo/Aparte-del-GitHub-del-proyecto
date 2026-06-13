package com.example.ms_registro.de.materiales.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.*;

import lombok.RequiredArgsConstructor;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.ms_registro.de.materiales.dto.ApiResponse;
import com.example.ms_registro.de.materiales.security.JwtFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final ObjectMapper objectMapper; // ✅ Fix #1: inyectado como bean, no instanciado por request

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    // ✅ Fix #3: rutas públicas sin autenticación
                    .requestMatchers("/auth/**", "/actuator/health", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                    .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                    .accessDeniedHandler(accessDeniedHandler())
                    .authenticationEntryPoint(authenticationEntryPoint())
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8"); // ✅ Fix #2: encoding explícito para caracteres españoles

            ApiResponse<Object> res = ApiResponse.builder()
                    .success(false)
                    .message("Acceso denegado")
                    .build();

            objectMapper.writeValue(response.getOutputStream(), res); // ✅ Fix #1: usar bean inyectado
        };
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8"); // ✅ Fix #2: encoding explícito para caracteres españoles

            ApiResponse<Object> res = ApiResponse.builder()
                    .success(false)
                    .message("No autenticado o token inválido")
                    .build();

            objectMapper.writeValue(response.getOutputStream(), res); // ✅ Fix #1: usar bean inyectado
        };
    }
}