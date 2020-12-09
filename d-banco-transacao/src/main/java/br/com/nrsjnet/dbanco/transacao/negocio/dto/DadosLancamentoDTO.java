package br.com.nrsjnet.dbanco.transacao.negocio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DadosLancamentoDTO {

    private BigDecimal valor;
}
