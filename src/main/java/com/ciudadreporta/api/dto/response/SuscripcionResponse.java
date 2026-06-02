package com.ciudadreporta.api.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record SuscripcionResponse(
    UUID id,
    UUID usuarioId,
    String usuarioNombre,
    Long categoriaId,
    String categoriaNombre,
    LocalDateTime createdAt
) {}
