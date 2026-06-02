package com.ciudadreporta.api.dto.response;

import java.util.UUID;

public record VotoResponse(
    UUID reporteId,
    long upvotes,
    long downvotes,
    String mensaje
) {}
