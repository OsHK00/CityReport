package com.ciudadreporta.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciudadreporta.api.dto.request.CategoriaRequest;
import com.ciudadreporta.api.dto.response.CategoriaResponse;
import com.ciudadreporta.api.model.CategoriaModel;
import com.ciudadreporta.api.repository.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<CategoriaResponse> obtenerTodas() {
        List<CategoriaModel> categorias = (List<CategoriaModel>) categoriaRepository.findAll();
        List<CategoriaResponse> response = new ArrayList<>();
        for (CategoriaModel c : categorias) {
            response.add(toCategoriaResponse(c));
        }
        return response;
    }

    public CategoriaResponse obtenerPorId(Long id) {
        CategoriaModel categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        return toCategoriaResponse(categoria);
    }

    public CategoriaResponse crear(CategoriaRequest request) {
        CategoriaModel categoria = CategoriaModel.builder()
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .build();
        CategoriaModel guardado = categoriaRepository.save(categoria);
        return toCategoriaResponse(guardado);
    }

    public CategoriaResponse actualizar(Long id, CategoriaRequest request) {
        CategoriaModel categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        categoria.setNombre(request.nombre());
        categoria.setDescripcion(request.descripcion());
        CategoriaModel guardado = categoriaRepository.save(categoria);
        return toCategoriaResponse(guardado);
    }

    public void eliminar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoria no encontrada");
        }
        categoriaRepository.deleteById(id);
    }

    private CategoriaResponse toCategoriaResponse(CategoriaModel categoria) {
        return new CategoriaResponse(
            categoria.getId(),
            categoria.getNombre(),
            categoria.getDescripcion()
        );
    }
}
