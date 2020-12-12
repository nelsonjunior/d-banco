package br.com.nrsjnet.dbanco.balanco.dominio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContaDTO {
    private String uuid;
    private String nomeCompleto;
    private String cpf;
    private BigDecimal saldo = BigDecimal.ZERO;
}

