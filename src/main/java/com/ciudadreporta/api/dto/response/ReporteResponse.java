package com.ciudadreporta.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ReporteResponse(
    UUID id,
    String titulo,
    String descripcion,
    Double latitud,
    Double longitud,
    CategoriaInfo categoria,
    UsuarioInfo usuario,
    List<ImagenInfo> imagenes,
    long upvotes,
    long downvotes,
    LocalDateTime createdAt
) {
    public record CategoriaInfo(Long id, String nombre) {}
    public record UsuarioInfo(UUID id, String nombre) {}
    public record ImagenInfo(Long id, String url, Integer orden) {}
}
