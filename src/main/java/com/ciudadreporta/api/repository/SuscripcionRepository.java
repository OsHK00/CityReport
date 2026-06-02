package com.ciudadreporta.api.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ciudadreporta.api.model.CategoriaModel;
import com.ciudadreporta.api.model.SuscripcionModel;
import com.ciudadreporta.api.model.UsuarioModel;

@Repository
public interface SuscripcionRepository extends CrudRepository<SuscripcionModel, UUID> {

    List<SuscripcionModel> findByUsuario(UsuarioModel usuario);

    List<SuscripcionModel> findByCategoria(CategoriaModel categoria);

    Optional<SuscripcionModel> findByUsuarioAndCategoria(UsuarioModel usuario, CategoriaModel categoria);

    boolean existsByUsuarioAndCategoria(UsuarioModel usuario, CategoriaModel categoria);

}
