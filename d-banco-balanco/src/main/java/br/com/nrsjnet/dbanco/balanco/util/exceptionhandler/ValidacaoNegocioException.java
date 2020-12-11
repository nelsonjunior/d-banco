package br.com.nrsjnet.dbanco.balanco.util.exceptionhandler;

public class ValidacaoNegocioException extends RuntimeException {

    public ValidacaoNegocioException(String mensagem) {
        super(mensagem);
    }

}