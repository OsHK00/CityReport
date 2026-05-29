package com.ciudadreporta.api.dto.response;

import java.util.UUID;

public record AuthResponse(
    UUID id,
    String nombre,
    String email,
    Boolean emailVerified,
    String mensaje
) {}
