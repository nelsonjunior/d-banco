package br.com.nrsjnet.dbanco.transacao.negocio.dto;

import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Conta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
public class DadosLancamentoDepositoDTO extends DadosLancamentoDTO {

    private Optional<Conta> conta;

}
