package br.com.softblue.bluebank.domain.contaBancaria;

@SuppressWarnings("serial")
public class ContaInexistenteException extends Exception {

    public ContaInexistenteException(String message) {
	super(message);
    }
}
