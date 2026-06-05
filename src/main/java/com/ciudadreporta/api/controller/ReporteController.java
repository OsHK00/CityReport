package com.ciudadreporta.api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ciudadreporta.api.dto.request.ReporteRequest;
import com.ciudadreporta.api.dto.request.VotoRequest;
import com.ciudadreporta.api.dto.response.FeedResponse;
import com.ciudadreporta.api.dto.response.ReporteResponse;
import com.ciudadreporta.api.dto.response.ReporteResponse.ImagenInfo;
import com.ciudadreporta.api.dto.response.VotoResponse;
import com.ciudadreporta.api.service.ReporteService;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping
    public FeedResponse obtenerFeed(@RequestParam(required = false) UUID cursor,
                                    @RequestParam(defaultValue = "10") int size) {
        return reporteService.obtenerFeed(cursor, size);
    }

    @GetMapping("/{id}")
    public ReporteResponse obtenerPorId(@PathVariable UUID id) {
        return reporteService.obtenerPorId(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ReporteResponse crear(
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("latitud") Double latitud,
            @RequestParam("longitud") Double longitud,
            @RequestParam("categoriaId") Long categoriaId,
            @RequestParam("usuarioId") String usuarioId,
            @RequestParam(value = "archivos", required = false) List<MultipartFile> archivos) {
        ReporteRequest request = new ReporteRequest(titulo, descripcion, latitud, longitud, categoriaId, usuarioId);
        return reporteService.crear(request, archivos);
    }

    @PutMapping("/{id}")
    public ReporteResponse actualizar(@PathVariable UUID id, @RequestBody ReporteRequest request) {
        return reporteService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable UUID id) {
        reporteService.eliminar(id);
    }

    @PostMapping("/{id}/votos")
    public VotoResponse votar(@PathVariable UUID id, @RequestBody VotoRequest request) {
        return reporteService.votar(id, UUID.fromString(request.usuarioId()), request.tipo());
    }

    @PostMapping("/{id}/imagenes")
    public List<ImagenInfo> subirImagenes(@PathVariable UUID id,
                                           @RequestParam("archivos") List<MultipartFile> archivos) {
        return reporteService.agregarImagenes(id, archivos);
    }
}
