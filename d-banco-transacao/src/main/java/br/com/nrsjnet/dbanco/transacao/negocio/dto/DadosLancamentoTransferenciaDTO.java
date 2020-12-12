package br.com.nrsjnet.dbanco.transacao.negocio.dto;

import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Conta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
public class DadosLancamentoTransferenciaDTO extends DadosLancamentoDTO {

    private Optional<Conta> contaOrigem;
    private Optional<Conta> contaDestino;

}
