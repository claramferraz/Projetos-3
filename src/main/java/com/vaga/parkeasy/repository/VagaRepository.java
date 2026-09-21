package com.vaga.parkeasy.repository;

import com.vaga.parkeasy.model.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VagaRepository extends JpaRepository<Vaga, Long> {
    // Se quiser filtro por nome de endereço:
    // List<Vaga> findByEnderecoContainingIgnoreCase(String endereco);
}