package com.ciudadreporta.api.dto.request;

public record AuthRequest(
    String email,
    String password
) {}
