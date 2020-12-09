package br.com.nrsjnet.dbanco.transacao.service.exceptions;

import br.com.nrsjnet.dbanco.transacao.util.exceptionhandler.ValidacaoNegocioException;

public class ValorNaoPermitidoException extends ValidacaoNegocioException {
    public ValorNaoPermitidoException() {
        super("Valor não permitido!");
    }
}
