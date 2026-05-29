package com.ciudadreporta.api.model;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "roles")
public class RolModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "rol")
    @JsonIgnore
    private List<UsuarioModel> usuarios;

    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private Integer NivelAcceso;
    
}
