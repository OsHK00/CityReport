package com.ciudadreporta.api.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ciudadreporta.api.model.ReporteModel;

@Repository
public interface ReporteRepository extends JpaRepository<ReporteModel, UUID> {

    @Query("SELECT r FROM ReporteModel r ORDER BY r.createdAt DESC, r.id DESC")
    List<ReporteModel> findFirstPage(Pageable pageable);

    @Query("SELECT r FROM ReporteModel r WHERE " +
           "r.createdAt < (SELECT r2.createdAt FROM ReporteModel r2 WHERE r2.id = :cursor) " +
           "OR (r.createdAt = (SELECT r2.createdAt FROM ReporteModel r2 WHERE r2.id = :cursor) AND r.id < :cursor) " +
           "ORDER BY r.createdAt DESC, r.id DESC")
    List<ReporteModel> findNextPage(@Param("cursor") UUID cursor, Pageable pageable);
}
