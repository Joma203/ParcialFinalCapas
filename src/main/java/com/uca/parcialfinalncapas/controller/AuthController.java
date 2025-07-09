// src/main/java/com/uca/parcialfinalncapas/controller/AuthController.java
package com.uca.parcialfinalncapas.controller;

import com.uca.parcialfinalncapas.dto.request.AuthRequest;
import com.uca.parcialfinalncapas.dto.request.RegisterRequest;
import com.uca.parcialfinalncapas.dto.response.AuthResponse;
import com.uca.parcialfinalncapas.entities.User;
import com.uca.parcialfinalncapas.repository.UserRepository;
import com.uca.parcialfinalncapas.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private AuthenticationManager authManager;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepository.findByCorreo(req.getCorreo()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: correo ya registrado");
        }
        String rol = req.getNombreRol().toUpperCase();
        if (!rol.equals("USER") && !rol.equals("TECH")) {
            return ResponseEntity.badRequest().body("Error: nombreRol debe ser USER o TECH");
        }
        User nuevo = User.builder()
                .nombre(req.getNombre())
                .correo(req.getCorreo())
                .password(passwordEncoder.encode(req.getPassword()))
                .nombreRol(rol)
                .build();
        userRepository.save(nuevo);
        String token = jwtUtil.generateToken(nuevo.getCorreo());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getCorreo(), req.getPassword())
            );
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
        UserDetails user = userDetailsService.loadUserByUsername(req.getCorreo());
        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
