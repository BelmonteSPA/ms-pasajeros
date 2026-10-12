package com.belmonte.ms_pasajeros.controller;

import com.belmonte.ms_pasajeros.dto.PasajeroRequestDTO;
import com.belmonte.ms_pasajeros.dto.PasajeroResponseDTO;
import com.belmonte.ms_pasajeros.service.PasajeroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pasajeros")
public class PasajeroController {
    private final PasajeroService pasajeroService;

    @Autowired
    public PasajeroController(PasajeroService pasajeroService) {
        this.pasajeroService = pasajeroService;
    }

    //***CRUD***
    //GET /pasajeros
    @GetMapping
    public ResponseEntity<List<PasajeroResponseDTO>> getTodos() {
        return ResponseEntity.ok(pasajeroService.mostrarPasajeros());
    }

    //GET /pasajeros/id  (404 si no existe)
    @GetMapping("{id}")
    public ResponseEntity<PasajeroResponseDTO> getPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pasajeroService.mostrarPorId(id));
    }

    //POST /pasajeros  (400 si hay errores de validacion, 409 si el email ya existe)
    @PostMapping
    public ResponseEntity<PasajeroResponseDTO> postPasajero(@Valid @RequestBody PasajeroRequestDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pasajeroService.save(dto));
    }

    //PUT /pasajeros/id  (preserva el estado activo)
    @PutMapping("{id}")
    public ResponseEntity<PasajeroResponseDTO> putPasajero(
            @PathVariable Long id,
            @Valid @RequestBody PasajeroRequestDTO dto
    ) {
        return ResponseEntity.ok(pasajeroService.editar(id, dto));
    }

    //DELETE /pasajeros/id
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePasajero(@PathVariable Long id) {
        pasajeroService.borrar(id);
        return ResponseEntity.noContent().build();
    }

    //***EXTRAS**
    //PATCH /pasajeros/id/estado
    //alterna el estado: si esta activo (1) pasa a inactivo (0) y viceversa
    @PatchMapping("{id}/estado")
    public ResponseEntity<PasajeroResponseDTO> patchEstado(@PathVariable Long id) {
        return ResponseEntity.ok(pasajeroService.cambiarEstado(id));
    }

    //GET /pasajeros/id/validar
    //devuelve true si el pasajero existe y esta activo (lo consume ms-viajes via Feign)
    @GetMapping("{id}/validar")
    public ResponseEntity<Boolean> validar(@PathVariable Long id) {
        return ResponseEntity.ok(pasajeroService.validarPasajero(id));
    }

    //GET /pasajeros/activos
    @GetMapping("activos")
    public ResponseEntity<List<PasajeroResponseDTO>> getActivos() {
        return ResponseEntity.ok(pasajeroService.mostrarActivos());
    }

    //GET /pasajeros/empresa/{empresa}  (busqueda parcial, sin distinguir mayusculas)
    @GetMapping("empresa/{empresa}")
    public ResponseEntity<List<PasajeroResponseDTO>> getPorEmpresa(@PathVariable String empresa) {
        return ResponseEntity.ok(pasajeroService.mostrarPorEmpresa(empresa));
    }
}
