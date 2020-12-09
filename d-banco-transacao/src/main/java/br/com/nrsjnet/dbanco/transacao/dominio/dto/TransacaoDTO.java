package br.com.nrsjnet.dbanco.transacao.dominio.dto;

import br.com.nrsjnet.dbanco.transacao.dominio.enums.TipoTransacaoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoDTO {

    private TipoTransacaoEnum tipoTransacao;
    private BigDecimal valorInformado;
    private BigDecimal valorRealizado;
    private BigDecimal valorTaxas;
    private BigDecimal valorBonus;
    private BigDecimal valorSaldo;
    private LocalDate dataTransacao;
}
