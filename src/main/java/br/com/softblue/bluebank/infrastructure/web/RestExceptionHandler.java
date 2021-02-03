package br.com.softblue.bluebank.infrastructure.web;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.softblue.bluebank.domain.contaBancaria.ContaInexistenteException;
import br.com.softblue.bluebank.domain.contaBancaria.SaldoInsufucienteException;
import br.com.softblue.bluebank.domain.contaBancaria.ValorNegativoException;
import br.com.softblue.bluebank.domain.usuario.CpfExistenteException;
import br.com.softblue.bluebank.domain.usuario.EmailExistenteException;
import br.com.softblue.bluebank.domain.usuario.TitularExistenteException;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
	    HttpHeaders headers, HttpStatus status, WebRequest request) {

	List<ObjectError> errors = RestResponseError.getErrorsValidation(ex);

	return new ResponseEntity<>(RestResponseError.fromValidationError(errors), status);
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public RestResponseError handlerContaInexistenteException(ContaInexistenteException e) {
	return RestResponseError.fromMEssage(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public RestResponseError handlerValorNEgativoException(ValorNegativoException e) {
	return RestResponseError.fromMEssage(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public RestResponseError handlerSaldoInsuficienteValorNEgativoException(SaldoInsufucienteException e) {
	return RestResponseError.fromMEssage(e.getMessage());
    }
    
    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public RestResponseError handlerTitularExistenteException(TitularExistenteException e) {
	return RestResponseError.fromMEssage(e.getMessage());
    }
    
    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public RestResponseError handlerEmailExistenteException(EmailExistenteException e) {
	return RestResponseError.fromMEssage(e.getMessage());
    }
    
    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public RestResponseError handlerCpfExistenteException(CpfExistenteException e) {
	return RestResponseError.fromMEssage(e.getMessage());
    }
}
