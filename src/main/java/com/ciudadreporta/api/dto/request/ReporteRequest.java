package com.ciudadreporta.api.dto.request;

public record ReporteRequest(
    String titulo,
    String descripcion,
    Double latitud,
    Double longitud,
    Long categoriaId,
    String usuarioId
) {}
