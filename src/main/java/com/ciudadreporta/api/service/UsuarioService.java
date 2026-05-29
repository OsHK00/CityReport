package com.ciudadreporta.api.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciudadreporta.api.repository.UsuarioRepository;
import com.ciudadreporta.api.repository.RolRepository;
import com.ciudadreporta.api.model.UsuarioModel;
import com.ciudadreporta.api.model.RolModel;
import com.ciudadreporta.api.config.AppConfig;
import com.ciudadreporta.api.dto.request.AuthRequest;
import com.ciudadreporta.api.dto.request.RegistroRequest;
import com.ciudadreporta.api.dto.request.ResendCodeRequest;
import com.ciudadreporta.api.dto.request.VerificacionRequest;
import com.ciudadreporta.api.dto.response.AuthResponse;
import com.ciudadreporta.api.dto.response.UsuarioResponse;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private EmailService emailService;

    private static final SecureRandom RANDOM = new SecureRandom();

    public List<UsuarioResponse> obtenerUsuarios() {
        List<UsuarioModel> usuarios = (List<UsuarioModel>) usuarioRepository.findAll();
        List<UsuarioResponse> response = new ArrayList<>();
        for (UsuarioModel u : usuarios) {
            response.add(toUsuarioResponse(u));
        }
        return response;
    }

    public AuthResponse registrar(RegistroRequest request) {
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("El email ya esta registrado");
        }

        RolModel rol = rolRepository.findById(request.rolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        boolean verificationEnabled = appConfig.getVerification().isEnabled();

        UsuarioModel usuario = UsuarioModel.builder()
                .nombre(request.nombre())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .activo(!verificationEnabled)
                .emailVerified(!verificationEnabled)
                .rol(rol)
                .build();

        if (verificationEnabled) {
            String codigo = generarCodigo();
            usuario.setCodigoVerificacion(codigo);
            UsuarioModel guardado = usuarioRepository.save(usuario);
            emailService.enviarCodigoVerificacion(guardado.getEmail(), codigo);
            return new AuthResponse(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getEmail(),
                false,
                "Registro exitoso. Revisa tu correo para verificar tu cuenta."
            );
        }

        UsuarioModel guardado = usuarioRepository.save(usuario);
        return new AuthResponse(
            guardado.getId(),
            guardado.getNombre(),
            guardado.getEmail(),
            true,
            "Registro exitoso"
        );
    }

    public AuthResponse verificar(VerificacionRequest request) {
        UsuarioModel usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getEmailVerified()) {
            return new AuthResponse(usuario.getId(), usuario.getNombre(), usuario.getEmail(), true, "Cuenta ya verificada");
        }

        if (usuario.getCodigoVerificacion() == null || !usuario.getCodigoVerificacion().equals(request.codigo())) {
            throw new RuntimeException("Codigo de verificacion invalido");
        }

        usuario.setEmailVerified(true);
        usuario.setActivo(true);
        usuario.setCodigoVerificacion(null);
        usuarioRepository.save(usuario);

        return new AuthResponse(usuario.getId(), usuario.getNombre(), usuario.getEmail(), true, "Cuenta verificada exitosamente");
    }

    public AuthResponse reenviarCodigo(ResendCodeRequest request) {
        UsuarioModel usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getEmailVerified()) {
            return new AuthResponse(usuario.getId(), usuario.getNombre(), usuario.getEmail(), true, "Cuenta ya verificada");
        }

        String codigo = generarCodigo();
        usuario.setCodigoVerificacion(codigo);
        usuarioRepository.save(usuario);
        emailService.enviarCodigoVerificacion(usuario.getEmail(), codigo);

        return new AuthResponse(usuario.getId(), usuario.getNombre(), usuario.getEmail(), false, "Codigo reenviado");
    }

    public AuthResponse login(AuthRequest request) {
        UsuarioModel usuario = usuarioRepository.findByEmail(request.email())
                .orElse(null);

        if (usuario == null) {
            return new AuthResponse(null, null, request.email(), null, "Credenciales invalidas");
        }

        if (!passwordEncoder.matches(request.password(), usuario.getPasswordHash())) {
            return new AuthResponse(null, null, request.email(), null, "Credenciales invalidas");
        }

        if (appConfig.getVerification().isEnabled() && !usuario.getEmailVerified()) {
            return new AuthResponse(usuario.getId(), usuario.getNombre(), usuario.getEmail(), false, "Debes verificar tu cuenta primero. Revisa tu correo.");
        }

        return new AuthResponse(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getEmail(),
            usuario.getEmailVerified(),
            "Login exitoso"
        );
    }

    public UsuarioModel getUsuarioById(UUID id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public UsuarioModel getUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    private String generarCodigo() {
        int codigo = 100000 + RANDOM.nextInt(900000);
        return String.valueOf(codigo);
    }

    private UsuarioResponse toUsuarioResponse(UsuarioModel usuario) {
        return new UsuarioResponse(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getEmail(),
            usuario.getActivo(),
            usuario.getRol().getNombre(),
            usuario.getCreatedAt()
        );
    }
}
