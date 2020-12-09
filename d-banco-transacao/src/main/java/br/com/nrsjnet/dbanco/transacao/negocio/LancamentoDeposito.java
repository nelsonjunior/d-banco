package br.com.nrsjnet.dbanco.transacao.negocio;

import br.com.nrsjnet.dbanco.transacao.dominio.dto.TransacaoDTO;
import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Conta;
import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Lancamento;
import br.com.nrsjnet.dbanco.transacao.dominio.enums.TipoLancamentoEnum;
import br.com.nrsjnet.dbanco.transacao.dominio.enums.TipoTransacaoEnum;
import br.com.nrsjnet.dbanco.transacao.service.exceptions.ContaNaoEncontradaException;
import br.com.nrsjnet.dbanco.transacao.negocio.dto.DadosLancamentoDepositoDTO;
import br.com.nrsjnet.dbanco.transacao.util.exceptionhandler.ValidacaoNegocioException;

import java.util.List;

public class LancamentoDeposito extends AbstractLancamento<DadosLancamentoDepositoDTO> {

    private Lancamento lancamentoDeposito;
    private Conta conta;

    public LancamentoDeposito(DadosLancamentoDepositoDTO dados) {
        super(dados);
    }

    @Override
    public TransacaoDTO obterRetornoTransacao() {
        TransacaoDTO transacao = new TransacaoDTO();
        transacao.setTipoTransacao(TipoTransacaoEnum.DEPOSITO);
        return preencherTransacaoBase(transacao, lancamentoDeposito, conta);
    }

    @Override
    protected void validar() throws ValidacaoNegocioException {
        if(dados.getConta().isEmpty()){
            throw new ContaNaoEncontradaException();
        }
        this.conta = dados.getConta().get();
    }

    @Override
    protected List<Lancamento> obterLancamentos() {
        TipoLancamentoEnum tipoLancamento = TipoLancamentoEnum.DEPOSITO;
        lancamentoDeposito = prepararLancamento(tipoLancamento, conta);
        lancamentoDeposito.setDescricao(tipoLancamento.obterMensagem(dados.getValor(), lancamentoDeposito.getValorBonus()));
        return List.of(lancamentoDeposito);
    }

    @Override
    protected List<Conta> contasParaAtualizar() {
        return List.of(atualizarConta());
    }

    private Conta atualizarConta() {
        conta.setSaldo(conta.getSaldo().add(lancamentoDeposito.getValorRealizado()));
        return conta;
    }

}
