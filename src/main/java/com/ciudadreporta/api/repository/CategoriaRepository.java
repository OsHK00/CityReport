package com.ciudadreporta.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ciudadreporta.api.model.CategoriaModel;

@Repository
public interface CategoriaRepository extends CrudRepository<CategoriaModel, Long> {

}
