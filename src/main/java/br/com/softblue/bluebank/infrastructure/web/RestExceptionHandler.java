package br.com.softblue.bluebank.infrastructure.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.softblue.bluebank.domain.contaBancaria.ContaInexistenteException;
import br.com.softblue.bluebank.domain.contaBancaria.SaldoInsufucienteException;
import br.com.softblue.bluebank.domain.contaBancaria.ValorNegativoException;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
	    HttpHeaders headers, HttpStatus status, WebRequest request) {
	List<ObjectError> errors = getErrors(ex);
	ErrorResponse errorResponse = getErrorResponse(ex, status, errors);
	return new ResponseEntity<>(errorResponse, status);
    }

    private ErrorResponse getErrorResponse(MethodArgumentNotValidException ex, HttpStatus status,
	    List<ObjectError> errors) {
	return new ErrorResponse("Requisição possui campos inválidos", status.value(), status.getReasonPhrase(),
		ex.getBindingResult().getObjectName(), errors);
    }

    private List<ObjectError> getErrors(MethodArgumentNotValidException ex) {
	return ex.getBindingResult().getFieldErrors().stream()
		.map(error -> new ObjectError(error.getDefaultMessage(), error.getField(), error.getRejectedValue()))
		.collect(Collectors.toList());
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

}
