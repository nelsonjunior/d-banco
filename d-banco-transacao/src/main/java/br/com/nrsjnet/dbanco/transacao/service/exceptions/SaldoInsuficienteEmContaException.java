package br.com.nrsjnet.dbanco.transacao.service.exceptions;

import br.com.nrsjnet.dbanco.transacao.util.exceptionhandler.ValidacaoNegocioException;

public class SaldoInsuficienteEmContaException extends ValidacaoNegocioException {
    public SaldoInsuficienteEmContaException() {
        super("Saldo insuficiente em conta para realizar saque!");
    }
}
