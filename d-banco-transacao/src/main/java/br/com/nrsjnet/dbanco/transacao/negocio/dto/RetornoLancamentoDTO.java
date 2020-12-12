package br.com.nrsjnet.dbanco.transacao.negocio.dto;

import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Conta;
import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Lancamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class RetornoLancamentoDTO {

    private List<Lancamento> lancamentosRealizados;
    private List<Conta> contasParaAtualizar;

}
