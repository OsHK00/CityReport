package com.ciudadreporta.api.event;

import java.util.UUID;

import org.springframework.context.ApplicationEvent;

public class ReporteCreadoEvent extends ApplicationEvent {

    private final UUID reporteId;
    private final Long categoriaId;

    public ReporteCreadoEvent(Object source, UUID reporteId, Long categoriaId) {
        super(source);
        this.reporteId = reporteId;
        this.categoriaId = categoriaId;
    }

    public UUID getReporteId() {
        return reporteId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }
}
