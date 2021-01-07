package br.com.softblue.bluebank.infrastructure.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ObjectError {

    private final String message;
    private final String field;
    private final Object parameter;
    
}
