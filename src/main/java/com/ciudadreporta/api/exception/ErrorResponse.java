package com.ciudadreporta.api.exception;

public record ErrorResponse(
    String error,
    int codigo
) {}
