package com.ciudadreporta.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ciudadreporta.api.dto.request.RolRequest;
import com.ciudadreporta.api.dto.response.RolResponse;
import com.ciudadreporta.api.service.RolService;

@RestController
@RequestMapping("/roles")
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping
    public List<RolResponse> obtenerTodos() {
        return rolService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public RolResponse obtenerPorId(@PathVariable Long id) {
        return rolService.obtenerPorId(id);
    }

    @PostMapping
    public RolResponse crear(@RequestBody RolRequest request) {
        return rolService.crear(request);
    }

    @PutMapping("/{id}")
    public RolResponse actualizar(@PathVariable Long id, @RequestBody RolRequest request) {
        return rolService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        rolService.eliminar(id);
    }
}
