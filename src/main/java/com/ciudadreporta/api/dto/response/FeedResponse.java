package com.ciudadreporta.api.dto.response;

import java.util.List;
import java.util.UUID;

public record FeedResponse(
    List<ReporteResponse> data,
    UUID nextCursor,
    boolean hasMore
) {}
