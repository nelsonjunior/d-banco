package br.com.nrsjnet.dbanco.balanco.service.exceptions;

import br.com.nrsjnet.dbanco.balanco.util.exceptionhandler.ValidacaoNegocioException;

public class ContaNaoEncontradaException extends ValidacaoNegocioException {
    public ContaNaoEncontradaException() {
        super("Conta não encontrada!");
    }
}
