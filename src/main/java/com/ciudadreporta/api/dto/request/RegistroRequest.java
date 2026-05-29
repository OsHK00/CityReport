package com.ciudadreporta.api.dto.request;

public record RegistroRequest(
    String nombre,
    String email,
    String password,
    Long rolId
) {}
