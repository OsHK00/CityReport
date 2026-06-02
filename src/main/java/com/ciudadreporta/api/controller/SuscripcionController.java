package com.ciudadreporta.api.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ciudadreporta.api.dto.response.SuscripcionResponse;
import com.ciudadreporta.api.service.SuscripcionService;

@RestController
@RequestMapping("/suscripciones")
public class SuscripcionController {

    @Autowired
    private SuscripcionService suscripcionService;

    @PostMapping
    public SuscripcionResponse suscribir(@RequestBody Map<String, Object> body) {
        UUID usuarioId = UUID.fromString((String) body.get("usuarioId"));
        Long categoriaId = Long.valueOf(body.get("categoriaId").toString());
        return suscripcionService.suscribir(usuarioId, categoriaId);
    }

    @DeleteMapping("/{id}")
    public void desuscribir(@PathVariable UUID id) {
        suscripcionService.desuscribir(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<SuscripcionResponse> obtenerPorUsuario(@PathVariable UUID usuarioId) {
        return suscripcionService.obtenerPorUsuario(usuarioId);
    }
}
