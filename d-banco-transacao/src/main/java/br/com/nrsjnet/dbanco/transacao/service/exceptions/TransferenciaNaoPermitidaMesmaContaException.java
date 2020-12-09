package br.com.nrsjnet.dbanco.transacao.service.exceptions;

import br.com.nrsjnet.dbanco.transacao.util.exceptionhandler.ValidacaoNegocioException;

public class TransferenciaNaoPermitidaMesmaContaException extends ValidacaoNegocioException {
    public TransferenciaNaoPermitidaMesmaContaException() {
        super("Não é permitido realizar transfência para contas iguais!");
    }
}
