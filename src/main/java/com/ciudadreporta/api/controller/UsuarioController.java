package com.ciudadreporta.api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ciudadreporta.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

import com.ciudadreporta.api.dto.request.RegistroRequest;
import com.ciudadreporta.api.dto.response.AuthResponse;
import com.ciudadreporta.api.dto.response.UsuarioResponse;
import com.ciudadreporta.api.model.UsuarioModel;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioResponse> obtenerUsuarios() {
        return usuarioService.obtenerUsuarios();
    }

    @PostMapping
    public AuthResponse guardarUsuario(@RequestBody RegistroRequest request) {
        return usuarioService.registrar(request);
    }

    @GetMapping("/{id}")
    public UsuarioResponse getUsuarioById(@PathVariable UUID id) {
        UsuarioModel usuario = this.usuarioService.getUsuarioById(id);
        if (usuario == null) return null;
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
