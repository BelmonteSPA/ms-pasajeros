package com.belmonte.ms_pasajeros.repository;

import com.belmonte.ms_pasajeros.model.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasajeroRepository extends JpaRepository<Pasajero, Long> {

    //***EXTRAS***
    //Buscar por email (unico)
    Optional<Pasajero> findByEmail(String email);

    //Verificar si un email ya esta registrado
    boolean existsByEmail(String email);

    //Buscar pasajeros activos
    List<Pasajero> findByActivoTrue();

    //Buscar por empresa
    @Query("SELECT p FROM Pasajero p WHERE LOWER(p.empresa) LIKE LOWER(CONCAT('%', :empresa, '%')) ORDER BY p.nombre")
    List<Pasajero> findByEmpresaContainingIgnoreCase(@Param("empresa") String empresa);
}
