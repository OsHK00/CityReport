package com.ciudadreporta.api.dto.request;

public record VerificacionRequest(
    String email,
    String codigo
) {}
