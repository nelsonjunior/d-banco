package br.com.nrsjnet.dbanco.transacao.util.exceptionhandler;

public class ValidacaoNegocioException extends RuntimeException {

    public ValidacaoNegocioException(String mensagem) {
        super(mensagem);
    }

}