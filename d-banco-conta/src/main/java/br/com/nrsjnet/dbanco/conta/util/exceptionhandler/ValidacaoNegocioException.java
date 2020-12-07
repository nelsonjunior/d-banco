package br.com.nrsjnet.dbanco.conta.util.exceptionhandler;

public class ValidacaoNegocioException extends RuntimeException {

    public ValidacaoNegocioException(String mensagem) {
        super(mensagem);
    }

}