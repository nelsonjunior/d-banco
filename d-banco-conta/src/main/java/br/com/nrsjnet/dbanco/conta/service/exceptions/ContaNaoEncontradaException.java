package br.com.nrsjnet.dbanco.conta.service.exceptions;

import br.com.nrsjnet.dbanco.conta.util.exceptionhandler.ValidacaoNegocioException;

public class ContaNaoEncontradaException extends ValidacaoNegocioException {
    public ContaNaoEncontradaException() {
        super("Conta não encontrada!");
    }
}
