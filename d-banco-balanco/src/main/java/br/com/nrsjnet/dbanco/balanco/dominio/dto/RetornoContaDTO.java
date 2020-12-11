package br.com.nrsjnet.dbanco.balanco.dominio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetornoContaDTO {
    private String nomeCompleto;
    private String cpf;
    private BigDecimal saldo;
}

