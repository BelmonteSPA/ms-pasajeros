package com.belmonte.ms_pasajeros.service;

import com.belmonte.ms_pasajeros.dto.PasajeroRequestDTO;
import com.belmonte.ms_pasajeros.dto.PasajeroResponseDTO;
import com.belmonte.ms_pasajeros.exception.EmailDuplicadoException;
import com.belmonte.ms_pasajeros.exception.PasajeroNoEncontradoException;
import com.belmonte.ms_pasajeros.model.Pasajero;
import com.belmonte.ms_pasajeros.repository.PasajeroRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PasajeroService {
    private final PasajeroRepository pasajeroRepository;

    @Autowired
    public PasajeroService(PasajeroRepository pasajeroRepository) {
        this.pasajeroRepository = pasajeroRepository;
    }

    //Mapeo Entidad -> ResponseDTO
    private PasajeroResponseDTO mapToDTO(Pasajero pasajero) {
        return new PasajeroResponseDTO(
                pasajero.getIdPasajero(),
                pasajero.getNombre(),
                pasajero.getEmail(),
                pasajero.getTelefono(),
                pasajero.getEmpresa(),
                pasajero.isActivo()
        );
    }

    //Mapeo RequestDTO -> Entidad
    private Pasajero mapToEntity(PasajeroRequestDTO dto) {
        return new Pasajero(
                null,
                dto.getNombre(),
                dto.getEmail(),
                dto.getTelefono(),
                dto.getEmpresa(),
                true
        );
    }

    private Pasajero buscarOFallar(Long id) {
        return pasajeroRepository
                .findById(id)
                .orElseThrow(() -> new PasajeroNoEncontradoException(id));
    }

    //***CRUD***
    //GET /pasajeros
    public List<PasajeroResponseDTO> mostrarPasajeros() {
        return pasajeroRepository
                .findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    //GET /pasajeros/id
    public PasajeroResponseDTO mostrarPorId(Long id) {
        return mapToDTO(buscarOFallar(id));
    }

    //POST /pasajeros
    public PasajeroResponseDTO save(PasajeroRequestDTO dto) {
        if (pasajeroRepository.existsByEmail(dto.getEmail())) {
            throw new EmailDuplicadoException(dto.getEmail());
        }
        Pasajero guardado = pasajeroRepository.save(mapToEntity(dto));
        log.info(">>> Pasajero {} registrado correctamente", guardado.getIdPasajero());
        return mapToDTO(guardado);
    }

    //PUT /pasajeros/id
    public PasajeroResponseDTO editar(Long id, PasajeroRequestDTO dto) {
        Pasajero existente = buscarOFallar(id);

        //Si cambia el email, no puede pertenecer a otro pasajero
        if (!existente.getEmail().equalsIgnoreCase(dto.getEmail())
                && pasajeroRepository.existsByEmail(dto.getEmail())) {
            throw new EmailDuplicadoException(dto.getEmail());
        }

        existente.setNombre(dto.getNombre());
        existente.setEmail(dto.getEmail());
        existente.setTelefono(dto.getTelefono());
        existente.setEmpresa(dto.getEmpresa());
        // activo no se toca porque se maneja con PATCH /{id}/estado
        return mapToDTO(pasajeroRepository.save(existente));
    }

    //DELETE /pasajeros/id
    public void borrar(Long id) {
        if (!pasajeroRepository.existsById(id)) {
            throw new PasajeroNoEncontradoException(id);
        }
        pasajeroRepository.deleteById(id);
    }

    //***EXTRAS***
    //PATCH /pasajeros/id/estado  (alterna activo <-> inactivo)
    public PasajeroResponseDTO cambiarEstado(Long id) {
        Pasajero pasajero = buscarOFallar(id);
        pasajero.setActivo(!pasajero.isActivo());
        return mapToDTO(pasajeroRepository.save(pasajero));
    }

    //GET /pasajeros/id/validar  (lo consume ms-viajes via Feign)
    //devuelve true si el pasajero existe y esta activo
    public boolean validarPasajero(Long id) {
        return buscarOFallar(id).isActivo();
    }

    //GET /pasajeros/activos
    public List<PasajeroResponseDTO> mostrarActivos() {
        return pasajeroRepository
                .findByActivoTrue()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    //GET /pasajeros/empresa/{empresa}
    public List<PasajeroResponseDTO> mostrarPorEmpresa(String empresa) {
        return pasajeroRepository
                .findByEmpresaContainingIgnoreCase(empresa)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
