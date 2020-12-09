package br.com.nrsjnet.dbanco.transacao.negocio.dto;

import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Conta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DadosLancamentoSaqueDTO extends DadosLancamentoDTO {

    private Optional<Conta> conta;

}
