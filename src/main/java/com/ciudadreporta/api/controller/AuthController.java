package com.ciudadreporta.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ciudadreporta.api.dto.request.AuthRequest;
import com.ciudadreporta.api.dto.request.RegistroRequest;
import com.ciudadreporta.api.dto.request.ResendCodeRequest;
import com.ciudadreporta.api.dto.request.VerificacionRequest;
import com.ciudadreporta.api.dto.response.AuthResponse;
import com.ciudadreporta.api.service.UsuarioService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public AuthResponse registrar(@RequestBody RegistroRequest request) {
        return usuarioService.registrar(request);
    }

    @PostMapping("/verify")
    public AuthResponse verificar(@RequestBody VerificacionRequest request) {
        return usuarioService.verificar(request);
    }

    @PostMapping("/resend-code")
    public AuthResponse reenviarCodigo(@RequestBody ResendCodeRequest request) {
        return usuarioService.reenviarCodigo(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return usuarioService.login(request);
    }
}
