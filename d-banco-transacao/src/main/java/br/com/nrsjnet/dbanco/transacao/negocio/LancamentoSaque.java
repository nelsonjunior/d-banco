package br.com.nrsjnet.dbanco.transacao.negocio;

import br.com.nrsjnet.dbanco.transacao.dominio.dto.TransacaoDTO;
import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Conta;
import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Lancamento;
import br.com.nrsjnet.dbanco.transacao.dominio.enums.TipoLancamentoEnum;
import br.com.nrsjnet.dbanco.transacao.dominio.enums.TipoTransacaoEnum;
import br.com.nrsjnet.dbanco.transacao.service.exceptions.ContaNaoEncontradaException;
import br.com.nrsjnet.dbanco.transacao.service.exceptions.SaldoInsuficienteEmContaException;
import br.com.nrsjnet.dbanco.transacao.negocio.dto.DadosLancamentoSaqueDTO;
import br.com.nrsjnet.dbanco.transacao.util.exceptionhandler.ValidacaoNegocioException;

import java.util.List;

public class LancamentoSaque extends AbstractLancamento<DadosLancamentoSaqueDTO> {

    private Lancamento lancamentoSaque;
    private Conta conta;

    public LancamentoSaque(DadosLancamentoSaqueDTO dados) {
        super(dados);
    }

    @Override
    public TransacaoDTO obterRetornoTransacao() {
        TransacaoDTO transacao = new TransacaoDTO();
        transacao.setTipoTransacao(TipoTransacaoEnum.SAQUE);
        return preencherTransacaoBase(transacao, lancamentoSaque, conta);
    }

    @Override
    protected void validar() throws ValidacaoNegocioException {
        if(dados.getConta().isEmpty()){
            throw new ContaNaoEncontradaException();
        }
        this.conta = dados.getConta().get();

        if(dados.getValor().compareTo(conta.getSaldo()) > 0){
            throw new SaldoInsuficienteEmContaException();
        }
    }

    @Override
    protected List<Lancamento> obterLancamentos() {
        TipoLancamentoEnum tipoLancamento = TipoLancamentoEnum.SAQUE;
        lancamentoSaque = prepararLancamento(tipoLancamento, conta);
        lancamentoSaque.setDescricao(tipoLancamento.obterMensagem(dados.getValor(), lancamentoSaque.getValorTaxas()));
        return List.of(lancamentoSaque);
    }

    @Override
    protected List<Conta> contasParaAtualizar() {
        return List.of(atualizarConta());
    }

    private Conta atualizarConta() {
        conta.setSaldo(conta.getSaldo().subtract(lancamentoSaque.getValorRealizado()));
        return conta;
    }

}
