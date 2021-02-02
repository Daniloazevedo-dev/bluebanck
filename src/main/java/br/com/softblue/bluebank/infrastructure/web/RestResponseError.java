package br.com.softblue.bluebank.infrastructure.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class RestResponseError {

    private String error;
    
    private RestResponseError() {

    }

    public String getError() {
	return error;
    }
    
    public static RestResponseError fromValidationError(List<ObjectError> errors) {
	
	RestResponseError resp = new RestResponseError();
	StringBuilder sb = new StringBuilder();
	
	for(ObjectError error : errors) {
	    sb.append(error.getDefaultMessage()).append(". ");
	}
	
	resp.error = sb.toString();
	
	return resp;
    }
    
    public static RestResponseError fromMEssage(String message) {
	RestResponseError resp = new RestResponseError();
	resp.error = message;
	
	return resp;
    }
    
    public static List<ObjectError> getErrorsValidation(MethodArgumentNotValidException ex) {
	return ex.getBindingResult().getFieldErrors().stream()
		.map(error -> new ObjectError(error.getField(), error.getDefaultMessage()))
		.collect(Collectors.toList());
    }
}
