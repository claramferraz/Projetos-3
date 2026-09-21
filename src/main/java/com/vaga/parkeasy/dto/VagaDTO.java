package com.vaga.parkeasy.dto;

public record VagaDTO(
    Long id,
    String nome,
    String endereco,
    double distanciaMetros,
    double precoHora,
    int vagasLivres
) {}