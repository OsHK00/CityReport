package com.ciudadreporta.api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ciudadreporta.api.model.TipoVoto;
import com.ciudadreporta.api.model.ReporteModel;
import com.ciudadreporta.api.model.UsuarioModel;
import com.ciudadreporta.api.model.VotoReporteModel;

@Repository
public interface VotoReporteRepository extends CrudRepository<VotoReporteModel, UUID> {

    Optional<VotoReporteModel> findByUsuarioAndReporte(UsuarioModel usuario, ReporteModel reporte);

    long countByReporteIdAndTipo(UUID reporteId, TipoVoto tipo);

}
