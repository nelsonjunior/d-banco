package br.com.nrsjnet.dbanco.transacao.service.exceptions;

import br.com.nrsjnet.dbanco.transacao.util.exceptionhandler.ValidacaoNegocioException;

public class ContaNaoEncontradaException extends ValidacaoNegocioException {
    public ContaNaoEncontradaException() {
        super("Conta não encontrada!");
    }
}
