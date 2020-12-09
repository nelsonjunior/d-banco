package br.com.nrsjnet.dbanco.transacao.negocio;

import br.com.nrsjnet.dbanco.transacao.dominio.command.Command;
import br.com.nrsjnet.dbanco.transacao.dominio.command.DepositarCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.command.SaqueCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.command.TransferenciaCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.enums.TipoTransacaoEnum;
import br.com.nrsjnet.dbanco.transacao.repository.ContaRepository;
import br.com.nrsjnet.dbanco.transacao.negocio.dto.DadosLancamentoDepositoDTO;
import br.com.nrsjnet.dbanco.transacao.negocio.dto.DadosLancamentoSaqueDTO;
import br.com.nrsjnet.dbanco.transacao.negocio.dto.DadosLancamentoTransferenciaDTO;
import org.springframework.stereotype.Component;

@Component
public class LancamentoFactory {

    private final ContaRepository contaRepository;

    public LancamentoFactory(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public AbstractLancamento factory(TipoTransacaoEnum tipoTransacao, Command command){
        switch (tipoTransacao){
            case DEPOSITO: {
                DepositarCommand depositarCommand = (DepositarCommand) command;

                DadosLancamentoDepositoDTO dados = new DadosLancamentoDepositoDTO();
                dados.setConta(contaRepository.findByCpf(depositarCommand.getCpf()));
                dados.setValor(depositarCommand.getValor());

                return new LancamentoDeposito(dados);
            }
            case SAQUE: {
                SaqueCommand saqueCommand = (SaqueCommand) command;

                DadosLancamentoSaqueDTO dados = new DadosLancamentoSaqueDTO();
                dados.setConta(contaRepository.findByCpf(saqueCommand.getCpf()));
                dados.setValor(saqueCommand.getValor());

                return new LancamentoSaque(dados);
            }
            case TRANSFERENCIA: {
                TransferenciaCommand transferenciaCommand = (TransferenciaCommand) command;

                DadosLancamentoTransferenciaDTO dados = new DadosLancamentoTransferenciaDTO();
                dados.setContaOrigem(contaRepository.findByCpf(transferenciaCommand.getCpfOrigem()));
                dados.setContaDestino(contaRepository.findByCpf(transferenciaCommand.getCpfDestino()));
                dados.setValor(transferenciaCommand.getValor());

                return new LancamentoTransferencia(dados);
            }

        }
        return null;
    }

}
