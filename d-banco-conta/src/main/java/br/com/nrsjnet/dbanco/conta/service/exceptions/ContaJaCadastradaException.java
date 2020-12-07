package br.com.nrsjnet.dbanco.conta.service.exceptions;

import br.com.nrsjnet.dbanco.conta.util.exceptionhandler.ValidacaoNegocioException;

public class ContaJaCadastradaException extends ValidacaoNegocioException {
    public ContaJaCadastradaException() {
        super("Conta já cadastrada!");
    }
}
