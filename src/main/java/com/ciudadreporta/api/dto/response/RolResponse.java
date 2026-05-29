package com.ciudadreporta.api.dto.response;

public record RolResponse(
    Long id,
    String nombre,
    Integer nivelAcceso
) {}
