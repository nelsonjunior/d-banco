package br.com.nrsjnet.dbanco.transacao.dominio.enums;

import br.com.nrsjnet.dbanco.transacao.dominio.command.TransacaoCommand;
import br.com.nrsjnet.dbanco.transacao.dominio.dto.ValoresTransacaoDTO;
import br.com.nrsjnet.dbanco.transacao.dominio.entidade.Conta;
import br.com.nrsjnet.dbanco.transacao.service.exceptions.SaldoInsuficienteEmContaException;
import br.com.nrsjnet.dbanco.transacao.service.exceptions.TransferenciaNaoPermitidaMesmaContaException;
import br.com.nrsjnet.dbanco.transacao.service.exceptions.ValorNaoPermitidoException;
import br.com.nrsjnet.dbanco.transacao.util.exceptionhandler.ValidacaoNegocioException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum TipoTransacaoEnum {

    DEPOSITO{
        @Override
        public ValoresTransacaoDTO calcularValores(TransacaoCommand command) {
            BigDecimal valorBonus = calcularBonusParaDeposito(command);
            BigDecimal valorRealizado = command.getValor().add(valorBonus);

            ValoresTransacaoDTO transacao = new ValoresTransacaoDTO();
            transacao.setValorInformado(command.getValor());
            transacao.setValorBonus(valorBonus);
            transacao.setValorRealizado(valorRealizado);
            transacao.setValorTaxas(BigDecimal.ZERO);
            return transacao;
        }

        @Override
        public BigDecimal atualizarSaldo(BigDecimal saldoAtual, BigDecimal valorRealizado) {
            return saldoAtual.add(valorRealizado);
        }

        @Override
        public void validarTransacao(Conta contaOrigem, Conta contaDestino, ValoresTransacaoDTO valores) throws ValidacaoNegocioException {
        }

        private BigDecimal calcularBonusParaDeposito(TransacaoCommand command) {
            return command.getValor().multiply(new BigDecimal("0.005")).setScale(2, RoundingMode.HALF_DOWN);
        }
    },
    SAQUE{
        @Override
        public ValoresTransacaoDTO calcularValores(TransacaoCommand command) {
            BigDecimal valorTaxas = calcularTaxasParaSaque(command);
            BigDecimal valorRealizado = command.getValor().add(valorTaxas);

            ValoresTransacaoDTO transacao = new ValoresTransacaoDTO();
            transacao.setValorInformado(command.getValor());
            transacao.setValorBonus(BigDecimal.ZERO);
            transacao.setValorRealizado(valorRealizado);
            transacao.setValorTaxas(valorTaxas);
            return transacao;
        }

        @Override
        public BigDecimal atualizarSaldo(BigDecimal saldoAtual, BigDecimal valorRealizado) {
            return saldoAtual.subtract(valorRealizado);
        }

        private BigDecimal calcularTaxasParaSaque(TransacaoCommand command) {
            return command.getValor().multiply(new BigDecimal("0.01")).setScale(2, RoundingMode.HALF_DOWN);
        }

        @Override
        public void validarTransacao(Conta contaOrigem, Conta contaDestino, ValoresTransacaoDTO valores) throws ValidacaoNegocioException {
            if(valores.getValorRealizado().compareTo(contaOrigem.getSaldo()) > 0){
                throw new SaldoInsuficienteEmContaException();
            }
        }
    },
    TRANSFERENCIA{
        @Override
        public ValoresTransacaoDTO calcularValores(TransacaoCommand command) {
            ValoresTransacaoDTO transacao = new ValoresTransacaoDTO();
            transacao.setValorInformado(command.getValor());
            transacao.setValorBonus(BigDecimal.ZERO);
            transacao.setValorRealizado(command.getValor());
            transacao.setValorTaxas(BigDecimal.ZERO);
            return transacao;
        }

        @Override
        public BigDecimal atualizarSaldo(BigDecimal saldoAtual, BigDecimal valorRealizado) {
            return SAQUE.atualizarSaldo(saldoAtual, valorRealizado);
        }

        @Override
        public void validarTransacao(Conta contaOrigem, Conta contaDestino, ValoresTransacaoDTO valores) throws ValidacaoNegocioException {
            SAQUE.validarTransacao(contaOrigem, contaDestino, valores);
            if(contaOrigem.equals(contaDestino)){
                throw new TransferenciaNaoPermitidaMesmaContaException();
            }
        }
    };

    public abstract ValoresTransacaoDTO calcularValores(TransacaoCommand command);

    public abstract  BigDecimal atualizarSaldo(BigDecimal saldoAtual, BigDecimal valorRealizado);

    abstract void validarTransacao(Conta contaOrigem, Conta contaDestino, ValoresTransacaoDTO valores) throws ValidacaoNegocioException;

    public void validar(Conta contaOrigem, Conta contaDestino, ValoresTransacaoDTO valores) throws ValidacaoNegocioException{
        validarTransacao(contaOrigem, contaDestino, valores);

        if(valores.getValorInformado().compareTo(BigDecimal.ZERO) < 0){
            throw new ValorNaoPermitidoException();
        }
    }
}
