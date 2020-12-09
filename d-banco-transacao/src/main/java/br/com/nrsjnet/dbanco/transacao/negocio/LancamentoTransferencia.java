package br.com.nrsjnet.dbanco.transacao.negocio;

import br.com.nrsjnet.dbanco.transacao.dominio.dto.NovaTransferenciaDTO;
import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Conta;
import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Lancamento;
import br.com.nrsjnet.dbanco.transacao.dominio.enums.TipoLancamentoEnum;
import br.com.nrsjnet.dbanco.transacao.dominio.enums.TipoTransacaoEnum;
import br.com.nrsjnet.dbanco.transacao.service.exceptions.ContaNaoEncontradaException;
import br.com.nrsjnet.dbanco.transacao.service.exceptions.SaldoInsuficienteEmContaException;
import br.com.nrsjnet.dbanco.transacao.service.exceptions.TransferenciaNaoPermitidaMesmaContaException;
import br.com.nrsjnet.dbanco.transacao.negocio.dto.DadosLancamentoTransferenciaDTO;
import br.com.nrsjnet.dbanco.transacao.util.exceptionhandler.ValidacaoNegocioException;

import java.util.List;

public class LancamentoTransferencia extends AbstractLancamento<DadosLancamentoTransferenciaDTO> {

    private Lancamento lancamentoDebito;
    private Lancamento lancamentoCredito;
    private Conta contaOrigem;
    private Conta contaDestino;

    public LancamentoTransferencia(DadosLancamentoTransferenciaDTO dados) {
        super(dados);
    }

    @Override
    public NovaTransferenciaDTO obterRetornoTransacao() {
        NovaTransferenciaDTO transacao = new NovaTransferenciaDTO();
        transacao.setTipoTransacao(TipoTransacaoEnum.TRANSFERENCIA);
        transacao.setDataTransacao(lancamentoDebito.getDataTransacao());
        transacao.setValorInformado(dados.getValor());
        transacao.setValorRealizado(dados.getValor());
        transacao.setValorTaxas(lancamentoCredito.getValorTaxas().add(lancamentoDebito.getValorTaxas()));
        transacao.setValorBonus(lancamentoCredito.getValorBonus().add(lancamentoDebito.getValorBonus()));
        transacao.setValorSaldo(contaOrigem.getSaldo());
        transacao.setCpfOrigem(contaOrigem.getCpf());
        transacao.setCpfDestino(contaDestino.getCpf());
        return transacao;
    }

    @Override
    protected void validar() throws ValidacaoNegocioException {
        if(dados.getContaOrigem().isEmpty()){
            throw new ContaNaoEncontradaException();
        }
        if(dados.getContaDestino().isEmpty()){
            throw new ContaNaoEncontradaException();
        }

        this.contaOrigem = dados.getContaOrigem().get();
        this.contaDestino = dados.getContaDestino().get();

        if(contaOrigem.equals(contaDestino)){
            throw new TransferenciaNaoPermitidaMesmaContaException();
        }
        if(dados.getValor().compareTo(contaOrigem.getSaldo()) > 0){
            throw new SaldoInsuficienteEmContaException();
        }

    }

    @Override
    protected List<Lancamento> obterLancamentos() {
        lancamentoDebito = prepararLancamento(TipoLancamentoEnum.DEBITO_TRANSFERENCIA, contaDestino);
        lancamentoDebito.setDescricao(TipoLancamentoEnum.DEBITO_TRANSFERENCIA.obterMensagem(dados.getValor(), contaDestino.getNomeCompleto()));
        lancamentoCredito = prepararLancamento(TipoLancamentoEnum.CREDITO_TRANSFERENCIA, contaOrigem);
        lancamentoCredito.setDescricao(TipoLancamentoEnum.CREDITO_TRANSFERENCIA.obterMensagem(dados.getValor(), contaOrigem.getNomeCompleto()));
        return List.of(lancamentoDebito, lancamentoCredito);
    }

    @Override
    protected List<Conta> contasParaAtualizar() {
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(lancamentoDebito.getValorRealizado()));
        contaDestino.setSaldo(contaDestino.getSaldo().add(lancamentoCredito.getValorRealizado()));
        return List.of(contaOrigem, contaDestino);
    }

}
