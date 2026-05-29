package com.ciudadreporta.api.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioResponse(
    UUID id,
    String nombre,
    String email,
    Boolean activo,
    String rol,
    LocalDateTime createdAt
) {}
