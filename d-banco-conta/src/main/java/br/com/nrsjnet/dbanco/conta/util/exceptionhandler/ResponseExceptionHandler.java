package br.com.nrsjnet.dbanco.conta.util.exceptionhandler;

import br.com.nrsjnet.dbanco.conta.dominio.dto.DetalheErroDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ControllerAdvice
@RestController
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private Logger LOGGER = Logger.getLogger("ResponseExceptionHandler");

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DetalheErroDTO> handleDefaultException(Exception ex, WebRequest request) {
        DetalheErroDTO detalheErro = new DetalheErroDTO(LocalDateTime.now(), ex.getMessage());
        LOGGER.severe(ex.getMessage());
        return new ResponseEntity(detalheErro, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ValidacaoNegocioException.class)
    public ResponseEntity<DetalheErroDTO> handleValidacaoNegocioException(Exception ex, WebRequest request) {
        DetalheErroDTO detalheErro = new DetalheErroDTO(LocalDateTime.now(), ex.getMessage());
        LOGGER.severe(ex.getMessage());
        return new ResponseEntity(detalheErro, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        DetalheErroDTO error = new DetalheErroDTO(LocalDateTime.now(), details);
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

}