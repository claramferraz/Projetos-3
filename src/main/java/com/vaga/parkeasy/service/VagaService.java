package com.vaga.parkeasy.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.vaga.parkeasy.dto.VagaDTO;
import com.vaga.parkeasy.dto.VagaRequest;
import com.vaga.parkeasy.model.Vaga;
import com.vaga.parkeasy.repository.VagaRepository;

@Service
public class VagaService {

    private final VagaRepository repository;

    public VagaService(VagaRepository repository) {
        this.repository = repository;
    }

    public List<VagaDTO> buscar(VagaRequest req) {
        List<Vaga> todas = repository.findAll();

        // Filtro por texto (endereço)
        if (req.local() != null && !req.local().isBlank()) {
            String filtro = req.local().trim().toLowerCase();
            todas = todas.stream()
                .filter(v -> v.getEndereco().toLowerCase().contains(filtro)
                          || v.getNome().toLowerCase().contains(filtro))
                .toList();
        }

        // Ordenação por distância (se lat/lng informados)
        if (req.lat() != null && req.lng() != null) {
            double lat = req.lat();
            double lng = req.lng();

            return todas.stream()
                .map(v -> toDTO(v, calcularDistancia(lat, lng, v.getLatitude(), v.getLongitude())))
                .sorted(Comparator.comparingDouble(VagaDTO::distanciaMetros))
                .toList();
        }

        return todas.stream().map(v -> toDTO(v, 0)).toList();
    }

    private VagaDTO toDTO(Vaga v, double distancia) {
        return new VagaDTO(
            v.getId(),
            v.getNome(),
            v.getEndereco(),
            distancia,
            v.getPrecoHora(),
            v.getVagasLivres()
        );
    }

    /** Fórmula de Haversine — distância em metros */
    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                 * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}