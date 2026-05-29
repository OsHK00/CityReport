package com.ciudadreporta.api.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

import com.ciudadreporta.api.model.UsuarioModel;

@Repository

public interface UsuarioRepository extends CrudRepository<UsuarioModel, UUID> {
    Optional<UsuarioModel> findByEmail(String email);
    Optional<UsuarioModel> findById(UUID id);
    
}
