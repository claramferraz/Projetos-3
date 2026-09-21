package com.vaga.parkeasy.dto;

import jakarta.validation.constraints.NotBlank;

public record VagaRequest(
    String local,
    Double lat,
    Double lng
) {}