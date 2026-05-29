package com.ciudadreporta.api.dto.request;

public record RolRequest(
    String nombre,
    Integer nivelAcceso
) {}
