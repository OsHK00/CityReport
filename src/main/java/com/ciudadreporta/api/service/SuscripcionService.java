package com.ciudadreporta.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciudadreporta.api.dto.response.SuscripcionResponse;
import com.ciudadreporta.api.model.CategoriaModel;
import com.ciudadreporta.api.model.SuscripcionModel;
import com.ciudadreporta.api.model.UsuarioModel;
import com.ciudadreporta.api.repository.CategoriaRepository;
import com.ciudadreporta.api.repository.SuscripcionRepository;
import com.ciudadreporta.api.repository.UsuarioRepository;

@Service
public class SuscripcionService {

    @Autowired
    private SuscripcionRepository suscripcionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional
    public SuscripcionResponse suscribir(UUID usuarioId, Long categoriaId) {
        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        CategoriaModel categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        if (suscripcionRepository.existsByUsuarioAndCategoria(usuario, categoria)) {
            throw new RuntimeException("Ya estas suscrito a esta categoria");
        }

        SuscripcionModel suscripcion = SuscripcionModel.builder()
                .usuario(usuario)
                .categoria(categoria)
                .build();

        SuscripcionModel guardado = suscripcionRepository.save(suscripcion);
        return toSuscripcionResponse(guardado);
    }

    @Transactional
    public void desuscribir(UUID id) {
        if (!suscripcionRepository.existsById(id)) {
            throw new RuntimeException("Suscripcion no encontrada");
        }
        suscripcionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<SuscripcionResponse> obtenerPorUsuario(UUID usuarioId) {
        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<SuscripcionModel> suscripciones = suscripcionRepository.findByUsuario(usuario);
        List<SuscripcionResponse> response = new ArrayList<>();
        for (SuscripcionModel s : suscripciones) {
            response.add(toSuscripcionResponse(s));
        }
        return response;
    }

    private SuscripcionResponse toSuscripcionResponse(SuscripcionModel suscripcion) {
        return new SuscripcionResponse(
            suscripcion.getId(),
            suscripcion.getUsuario().getId(),
            suscripcion.getUsuario().getNombre(),
            suscripcion.getCategoria().getId(),
            suscripcion.getCategoria().getNombre(),
            suscripcion.getCreatedAt()
        );
    }
}
