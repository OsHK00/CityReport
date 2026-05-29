package com.ciudadreporta.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciudadreporta.api.dto.request.RolRequest;
import com.ciudadreporta.api.dto.response.RolResponse;
import com.ciudadreporta.api.model.RolModel;
import com.ciudadreporta.api.repository.RolRepository;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public List<RolResponse> obtenerTodos() {
        List<RolModel> roles = (List<RolModel>) rolRepository.findAll();
        List<RolResponse> response = new ArrayList<>();
        for (RolModel r : roles) {
            response.add(toRolResponse(r));
        }
        return response;
    }

    public RolResponse obtenerPorId(Long id) {
        RolModel rol = rolRepository.findById(id).orElse(null);
        if (rol == null) return null;
        return toRolResponse(rol);
    }

    public RolResponse crear(RolRequest request) {
        RolModel rol = new RolModel();
        rol.setNombre(request.nombre());
        rol.setNivelAcceso(request.nivelAcceso());
        RolModel guardado = rolRepository.save(rol);
        return toRolResponse(guardado);
    }

    public RolResponse actualizar(Long id, RolRequest request) {
        RolModel rol = rolRepository.findById(id).orElse(null);
        if (rol == null) return null;
        rol.setNombre(request.nombre());
        rol.setNivelAcceso(request.nivelAcceso());
        RolModel guardado = rolRepository.save(rol);
        return toRolResponse(guardado);
    }

    public void eliminar(Long id) {
        rolRepository.deleteById(id);
    }

    private RolResponse toRolResponse(RolModel rol) {
        return new RolResponse(
            rol.getId(),
            rol.getNombre(),
            rol.getNivelAcceso()
        );
    }
}
