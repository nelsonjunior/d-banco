package br.com.nrsjnet.dbanco.balanco.dominio.dto;

import br.com.nrsjnet.dbanco.balanco.dominio.enums.TipoLancamentoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LancamentoDTO {

    private String uuid;

    private ContaDTO conta;

    private TipoLancamentoEnum tipoLancamento;

    private String descricao;

    private BigDecimal valorInformado;

    private BigDecimal valorTaxas;

    private BigDecimal valorBonus;

    private BigDecimal valorRealizado;

    private LocalDate dataTransacao;
}
