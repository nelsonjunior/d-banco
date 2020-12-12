package br.com.nrsjnet.dbanco.transacao.negocio;

import br.com.nrsjnet.dbanco.transacao.dominio.dto.TransacaoDTO;
import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Conta;
import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Lancamento;
import br.com.nrsjnet.dbanco.transacao.dominio.enums.TipoLancamentoEnum;
import br.com.nrsjnet.dbanco.transacao.service.exceptions.ValorNaoPermitidoException;
import br.com.nrsjnet.dbanco.transacao.negocio.dto.DadosLancamentoDTO;
import br.com.nrsjnet.dbanco.transacao.negocio.dto.RetornoLancamentoDTO;
import br.com.nrsjnet.dbanco.transacao.util.exceptionhandler.ValidacaoNegocioException;

import java.math.BigDecimal;
import java.util.List;

public abstract class AbstractLancamento<T extends DadosLancamentoDTO> {

    protected T dados;

    public AbstractLancamento(T dados){
        this.dados = dados;
    }

    protected BigDecimal obterValorRealizado(Lancamento lancamento){
        return lancamento.getValorInformado().add(lancamento.getValorBonus()).add(lancamento.getValorTaxas());
    }

    public RetornoLancamentoDTO prepararLancamento(){

        validarBase();

        validar();

        return new RetornoLancamentoDTO(obterLancamentos(), contasParaAtualizar());
    }

    private void validarBase() throws ValidacaoNegocioException{
        if(dados.getValor().compareTo(BigDecimal.ZERO) < 0){
            throw new ValorNaoPermitidoException();
        }
    }

    protected Lancamento prepararLancamento(TipoLancamentoEnum tipoLancamentoEnum, Conta conta) {
        var lancamento = new Lancamento();
        lancamento.setTipoLancamento(tipoLancamentoEnum);
        lancamento.setConta(conta);
        lancamento.setValorInformado(dados.getValor());
        lancamento.setValorBonus(tipoLancamentoEnum.valorBonus(dados.getValor()));
        lancamento.setValorTaxas(tipoLancamentoEnum.valorTaxas(dados.getValor()));
        lancamento.setValorRealizado(obterValorRealizado(lancamento));
        return lancamento;
    }

    protected TransacaoDTO preencherTransacaoBase(TransacaoDTO transacao, Lancamento lancamentoSaque, Conta conta) {
        transacao.setDataTransacao(lancamentoSaque.getDataTransacao());
        transacao.setValorInformado(lancamentoSaque.getValorInformado());
        transacao.setValorRealizado(lancamentoSaque.getValorRealizado());
        transacao.setValorTaxas(lancamentoSaque.getValorTaxas());
        transacao.setValorBonus(lancamentoSaque.getValorBonus());
        transacao.setValorSaldo(conta.getSaldo());
        return transacao;
    }

    public abstract TransacaoDTO obterRetornoTransacao();

    protected abstract void validar() throws ValidacaoNegocioException;

    protected abstract List<Lancamento> obterLancamentos();

    protected abstract List<Conta> contasParaAtualizar();

}
