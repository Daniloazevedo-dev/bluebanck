package br.com.softblue.bluebank.infrastructure.web;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ErrorResponse {
    
    private final String message;
    private final int code;
    private final String status;
    private final String objectName;
    private final List<ObjectError> errors;

}
