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
public class TransferenciaDTO extends TransacaoDTO {

    private String cpfOrigem;
    private String cpfDestino;
}
