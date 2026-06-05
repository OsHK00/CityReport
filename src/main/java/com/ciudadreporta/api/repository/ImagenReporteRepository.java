package com.ciudadreporta.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ciudadreporta.api.model.ImagenReporteModel;

@Repository
public interface ImagenReporteRepository extends CrudRepository<ImagenReporteModel, Long> {
}
